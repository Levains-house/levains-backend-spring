package com.levainshouse.mendolong.service;

import com.levainshouse.mendolong.dto.item.ItemGetRecommendResponse;
import com.levainshouse.mendolong.dto.item.ItemRegisterRequest;
import com.levainshouse.mendolong.dto.item.ItemUpdateTradeStatusRequest;
import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.enums.ItemTradeStatus;
import com.levainshouse.mendolong.exception.ItemNotFoundException;
import com.levainshouse.mendolong.file.FileStore;
import com.levainshouse.mendolong.file.UploadFile;
import com.levainshouse.mendolong.repository.AddressRepository;
import com.levainshouse.mendolong.repository.ItemCustomRepository;
import com.levainshouse.mendolong.repository.ItemRepository;
import com.levainshouse.mendolong.repository.condition.ItemRecommendExperienceSearchCondition;
import com.levainshouse.mendolong.repository.condition.ItemRecommendSearchCondition;
import com.levainshouse.mendolong.vo.ItemExperienceRecommendResponseVo;
import com.levainshouse.mendolong.vo.ItemRecommendResponseVo;
import com.levainshouse.mendolong.vo.ItemShareResponseVo;
import com.levainshouse.mendolong.vo.ItemWantResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private static final String TAG = "ITEM_SERVICE=%s";

    private final ItemRepository itemRepository;
    private final ItemCustomRepository itemCustomRepository;
    private final AddressRepository addressRepository;
    private final FileStore fileStore;
    private final DistanceService distanceService;

    public ItemGetRecommendResponse getRecommendItems(User user, int range, Pageable pageable){
        log.debug(String.format(TAG, "Search recommend items"));
        //?????? ???????????? ?????????????????? ????????????.
        List<Address> addresses = addressRepository.findByUser(user);
        //?????? ???????????? ?????????????????? ????????????.
        List<Address> oppositeAddresses
                = addressRepository.findByOppositeUserRole(user.getRole());
        //???????????? ?????? ??????????????? ????????????.
        List<User> validUsers = distanceService
                .getValidAddressesBetweenRange(addresses, oppositeAddresses, range);
        //????????? ???????????? ????????????.
        List<ItemCategory> itemCategories = itemRepository.findCategoriesByUserAndPurpose(user, ItemPurpose.WANT);
        //?????? ??????????????? ???????????? ???????????? ????????????.
        List<Item> searchShareItems = itemCustomRepository.searchRecommendItems(
                new ItemRecommendSearchCondition(validUsers, ItemPurpose.SHARE, itemCategories), pageable);
        //?????? ??????????????? ????????? ???????????? ????????????.
        List<Item> searchWantItems = itemRepository.searchItemsByUsersAndPurpose(validUsers, ItemPurpose.WANT);

        //VO??? ?????? ???????????????.
        List<ItemRecommendResponseVo> recommendItems = searchShareItems
                .stream()
                .map(si -> {

                            Random dice = new Random();
                            int n = dice.nextInt(searchWantItems.size());
                            while(searchWantItems.contains(si) && !si.getId().equals(searchWantItems.get(n).getId())){
                                n = dice.nextInt(searchWantItems.size());
                            }

                            return new ItemRecommendResponseVo(
                                    new ItemShareResponseVo(si),
                                    new ItemWantResponseVo(searchWantItems.get(n)));
                        }
                ).toList();

        //?????? ??????????????? ???????????? ????????? ??? ?????? ??????????????? ????????????.
        List<Item> searchRecommendExperienceItems = itemCustomRepository.searchRecommendExperienceItems(
                new ItemRecommendExperienceSearchCondition(validUsers, ItemPurpose.SHARE), pageable);

        List<ItemExperienceRecommendResponseVo> experienceRecommendItems = searchRecommendExperienceItems
                .stream()
                .map(ItemExperienceRecommendResponseVo::new)
                .collect(Collectors.toList());

        return new ItemGetRecommendResponse(recommendItems, experienceRecommendItems);
    }

    @Transactional
    public Item register(MultipartFile image, ItemRegisterRequest registerRequest, User user) throws IOException {
        log.debug(String.format(TAG, "Register"));
        //S3??? ????????? ?????????
        UploadFile uploadFile = fileStore.uploadFile(image);
        //DB??? ?????? ?????? ??????
        return itemRepository.save(Item.builder()
                .user(user)
                .name(registerRequest.getName())
                .description(registerRequest.getDescription())
                .imgName(uploadFile.storeFilename())
                .imgUrl(uploadFile.storeFileUrl())
                .category(registerRequest.getCategory())
                .purpose(registerRequest.getPurpose())
                .tradeStatus(ItemTradeStatus.BEFORE)
                .build());
    }

    public List<Item> getMyList(User user, Pageable pageable){
        log.debug(String.format(TAG, "My list"));

        return itemCustomRepository.searchMyItems(user, pageable);
    }

    @Transactional
    public Item updateTradeStatus(ItemUpdateTradeStatusRequest updateTradeStatusRequest){
        log.debug(String.format(TAG, "Update trade status"));

        Item item = itemRepository.findById(updateTradeStatusRequest.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("???????????? ?????? ???????????????."));
        return item.updateTradeStatus(updateTradeStatusRequest.getTradeStatus());
    }
}
