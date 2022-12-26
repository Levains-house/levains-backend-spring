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
import java.util.ArrayList;
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
        //현재 사용자의 주소정보들을 가져온다.
        List<Address> addresses = addressRepository.findByUser(user);
        //다른 사용자의 주소정보들을 가져온다.
        List<Address> oppositeAddresses
                = addressRepository.findByOppositeUserRole(user.getRole());
        //범위안에 있는 사용자들을 가져온다.
        List<User> validUsers = distanceService
                .getValidAddressesBetweenRange(addresses, oppositeAddresses, range);
        //원하는 물건들을 가져온다.
        List<ItemCategory> itemCategories = itemRepository.findCategoriesByUserAndPurpose(user, ItemPurpose.WANT);
        //다른 사용자들이 나눔하는 물건들을 가져온다.
        List<Item> searchShareItems = itemCustomRepository.searchRecommendItems(
                new ItemRecommendSearchCondition(validUsers, ItemPurpose.SHARE, itemCategories), pageable);
        //다른 사용자들이 원하는 물건들을 가져온다.
        List<Item> searchWantItems = itemRepository.searchItemsByUsersAndPurpose(validUsers, ItemPurpose.WANT);

        //VO에 모두 담아넣는다.
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

        //다른 사용자들이 나눔하는 물건들 중 체험 카테고리를 가져온다.
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
        //S3에 이미지 업로드
        UploadFile uploadFile = fileStore.uploadFile(image);
        //DB에 물건 정보 저장
        return itemRepository.save(Item.builder()
                .user(user)
                .name(registerRequest.getName())
                .description(registerRequest.getDescription())
                .imgName(uploadFile.getStoreFilename())
                .imgUrl(uploadFile.getStoreFileUrl())
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
                .orElseThrow(() -> new ItemNotFoundException("존재하지 않는 물건입니다."));
        return item.updateTradeStatus(updateTradeStatusRequest.getTradeStatus());
    }
}
