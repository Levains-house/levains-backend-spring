package com.levainshouse.mendolong.controller;

import com.levainshouse.mendolong.constant.WebProperties;
import com.levainshouse.mendolong.dto.SuccessResponse;
import com.levainshouse.mendolong.dto.item.*;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.service.ItemService;
import com.levainshouse.mendolong.service.TokenService;
import com.levainshouse.mendolong.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/items")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final TokenService tokenService;
    private final UserService userService;
    private final ItemService itemService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> search(
            @RequestHeader("authorization") String accessToken,
            @RequestParam("range") int range, Pageable pageable){
        User user = userService.getUser(tokenService.verify(accessToken));
        ItemGetRecommendResponse recommendItems = itemService.getRecommendItems(user, range, pageable);

        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        recommendItems
                ));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> register(
            @RequestHeader("authorization") String accessToken,
            @RequestPart(value = "itemImage", required = false) MultipartFile image,
            @Valid @RequestPart(value = "itemInfo") ItemRegisterRequest registerRequest) throws IOException {
        User user = userService.getUser(tokenService.verify(accessToken));
        Item item = itemService.register(image, registerRequest, user);

        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        new ItemRegisterResponse(item)
                ));
    }

    @GetMapping(value = "/my-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> searchMyList(
            @RequestHeader("authorization") String accessToken, Pageable pageable){
        User user = userService.getUser(tokenService.verify(accessToken));
        List<Item> items = itemService.getMyList(user, pageable);

        List<ItemGetMyListResponse> myItemListResponses = items.stream()
                .map(ItemGetMyListResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        myItemListResponses
                ));
    }

    @PutMapping(value = "/my-list/trade-status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updateTradeStatus(
            @RequestHeader("authorization") String accessToken,
            @RequestBody ItemUpdateTradeStatusRequest updateTradeStatusRequest){
        tokenService.verify(accessToken);
        Item updatedItem = itemService.updateTradeStatus(updateTradeStatusRequest);

        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        new ItemUpdateTradeStatusResponse(
                                updatedItem.getId(), updatedItem.getTradeStatus())
                ));
    }

}
