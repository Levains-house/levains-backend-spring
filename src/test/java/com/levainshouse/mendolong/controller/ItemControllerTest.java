package com.levainshouse.mendolong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levainshouse.mendolong.RestDocsConfig;
import com.levainshouse.mendolong.dto.address.AddressRegisterRequest;
import com.levainshouse.mendolong.dto.item.ItemRegisterRequest;
import com.levainshouse.mendolong.dto.item.ItemUpdateTradeStatusRequest;
import com.levainshouse.mendolong.dto.user.UserSignInRequest;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.enums.ItemTradeStatus;
import com.levainshouse.mendolong.enums.UserRole;
import com.levainshouse.mendolong.repository.ItemRepository;
import com.levainshouse.mendolong.service.AddressService;
import com.levainshouse.mendolong.service.ItemService;
import com.levainshouse.mendolong.service.TokenService;
import com.levainshouse.mendolong.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@Transactional
class ItemControllerTest {

    private String accessToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    void init(
            UserSignInRequest userSignInRequest,
            List<AddressRegisterRequest> addressRegisterRequests,
            List<ItemRegisterRequest> itemRegisterRequests) throws IOException {

        User user = userService.signIn(userSignInRequest);
        addressService.register(addressRegisterRequests, user);

        MockMultipartFile itemImage = new MockMultipartFile(
                "itemImage",
                "imagefile.png",
                "image/png",
                "<<png data>>".getBytes());

        for (ItemRegisterRequest itemRegisterRequest : itemRegisterRequests) {
            itemService.register(
                    itemImage,
                    itemRegisterRequest,
                    user
            );
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        //????????? 1 ??????
        User user = userService.signIn(
                new UserSignInRequest("?????? ?????????", "https://open.kakao.com/test", UserRole.TRAVEL));
        this.accessToken = tokenService.issue(user);

        List<AddressRegisterRequest> addressRegisterRequests = new ArrayList<>();
        addressRegisterRequests.add(new AddressRegisterRequest(33.51878087918451, 126.517879439631));
        addressRegisterRequests.add(new AddressRegisterRequest(33.3055316248359, 126.79208281479752));
        addressService.register(addressRegisterRequests, user);

        MockMultipartFile itemImage = new MockMultipartFile(
                "itemImage",
                "imagefile.png",
                "image/png",
                "<<png data>>".getBytes());

        List<ItemRegisterRequest> itemRegisterRequests = new ArrayList<>();
        itemRegisterRequests.add(new ItemRegisterRequest(
                "????????? ??????", "????????? ???????????????.", ItemCategory.THINGS, ItemPurpose.SHARE));
        itemRegisterRequests.add(new ItemRegisterRequest(
                "?????? ???", "?????? ??? ???????????????.", ItemCategory.BABY_THINGS, ItemPurpose.SHARE));
        itemRegisterRequests.add(new ItemRegisterRequest(
                "??? ?????? ??????", "??? ?????? ????????????.", ItemCategory.EXPERIENCE, ItemPurpose.WANT));
        itemRegisterRequests.add(new ItemRegisterRequest(
                "??????", "?????? ?????? ????????????.", ItemCategory.THINGS, ItemPurpose.WANT));

        for (ItemRegisterRequest itemRegisterRequest : itemRegisterRequests) {
            itemService.register(
                    itemImage,
                    itemRegisterRequest,
                    user
            );
        }

        //???????????? 1, 2, 3 ??????
        UserSignInRequest userSignInRequest2 =
                new UserSignInRequest("????????????1", "https://open.kakao.com/test", UserRole.LOCAL);
        List<AddressRegisterRequest> addressRegisterRequests2 = new ArrayList<>();
        addressRegisterRequests2.add(new AddressRegisterRequest(33.4932842519393, 126.498080317011));

        List<ItemRegisterRequest> itemRegisterRequests2 = new ArrayList<>();
        itemRegisterRequests2.add(new ItemRegisterRequest(
                "?????????", "????????? ?????? ?????? ??????????????????.", ItemCategory.THINGS, ItemPurpose.WANT));
        itemRegisterRequests2.add(new ItemRegisterRequest(
                "?????? ???", "?????? ?????? ??????????????????.", ItemCategory.BABY_THINGS, ItemPurpose.WANT));
        itemRegisterRequests2.add(new ItemRegisterRequest(
                "??? ?????? ??????", "??? ?????? ???????????????.", ItemCategory.EXPERIENCE, ItemPurpose.SHARE));
        itemRegisterRequests2.add(new ItemRegisterRequest(
                "??????", "??? ?????? ?????????.", ItemCategory.THINGS, ItemPurpose.SHARE));

        init(userSignInRequest2, addressRegisterRequests2, itemRegisterRequests2);
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void search() throws Exception {
        this.mockMvc.perform(
                        get("/api/items")
                                .header("authorization", accessToken)
                                .queryParam("range", "5")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("item-controller-test/search",
                        queryParameters(
                                parameterWithName("range").description("?????? ??????"),
                                parameterWithName("page").description("????????? ??????").optional(),
                                parameterWithName("size").description("????????? ??????").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.recommend_items").type(JsonFieldType.ARRAY)
                                        .description("?????? ??????"),
                                fieldWithPath("data.recommend_items[].share_item").type(JsonFieldType.OBJECT)
                                        .description("?????? ?????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].share_item.item_id").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ?????? ID").optional(),
                                fieldWithPath("data.recommend_items[].share_item.name").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].share_item.description").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????????").optional(),
                                fieldWithPath("data.recommend_items[].share_item.img_url").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????? URL").optional(),
                                fieldWithPath("data.recommend_items[].share_item.purpose").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].share_item.category").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????????").optional(),
                                fieldWithPath("data.recommend_items[].want_item").type(JsonFieldType.OBJECT)
                                        .description("????????? ????????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].want_item.item_id").type(JsonFieldType.NUMBER)
                                        .description("????????? ????????? ?????? ID").optional(),
                                fieldWithPath("data.recommend_items[].want_item.name").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].want_item.description").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ????????????").optional(),
                                fieldWithPath("data.recommend_items[].want_item.img_url").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ????????? URL").optional(),
                                fieldWithPath("data.recommend_items[].want_item.purpose").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ??????").optional(),
                                fieldWithPath("data.recommend_items[].want_item.category").type(JsonFieldType.STRING)
                                        .description("????????? ????????? ?????? ????????????").optional(),
                                fieldWithPath("data.experience_recommend_items").type(JsonFieldType.ARRAY)
                                        .description("?????? ?????? ??????"),
                                fieldWithPath("data.experience_recommend_items[].item_id").type(JsonFieldType.NUMBER)
                                        .description("?????? ?????? ?????? ID").optional(),
                                fieldWithPath("data.experience_recommend_items[].name").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ??????").optional(),
                                fieldWithPath("data.experience_recommend_items[].description").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????????").optional(),
                                fieldWithPath("data.experience_recommend_items[].img_url").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????? URL").optional(),
                                fieldWithPath("data.experience_recommend_items[].purpose").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ??????").optional(),
                                fieldWithPath("data.experience_recommend_items[].category").type(JsonFieldType.STRING)
                                        .description("?????? ?????? ?????? ????????????").optional()


                        )));
    }

    @Test
    @DisplayName("?????? ??????")
    void register() throws Exception {

        MockMultipartFile itemImage = new MockMultipartFile(
                "itemImage",
                "imagefile.png",
                "image/png",
                "<<png data>>".getBytes());

        MockMultipartFile itemInfo = new MockMultipartFile(
                "itemInfo",
                "",
                "application/json",
                ("{" +
                        "\"name\": \"????????? ??????\"," +
                        "\"description\": \"????????? ?????? ????????????\"," +
                        "\"category\": \"THINGS\"," +
                        "\"purpose\": \"SHARE\"" +
                "}").getBytes()
        );

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/items")
                        .file(itemImage)
                        .file(itemInfo)
                        .header("authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("item-controller-test/register",

                        requestParts(
                                partWithName("itemImage").description("?????? ?????????").optional(),
                                partWithName("itemInfo").description("?????? ??????")
                        ),
                        requestPartFields(
                                "itemInfo",
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("?????? ????????????")
                                        .attributes(Attributes.key("constraints")
                                                .value(String.format("%s,%s,%s,%s,%s,%s ??? ??? 1",
                                                ItemCategory.THINGS, ItemCategory.CLOTH,
                                                ItemCategory.BOOK, ItemCategory.LIVE_THINGS,
                                                ItemCategory.BABY_THINGS, ItemCategory.EXPERIENCE))),
                                fieldWithPath("purpose").type(JsonFieldType.STRING).description("?????? ??????")
                                        .attributes(Attributes.key("constraints")
                                                .value(String.format("%s,%s ??? ??? 1", ItemPurpose.SHARE, ItemPurpose.WANT)))

                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.item_id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("data.img_url").type(JsonFieldType.STRING).description("?????? ????????? URL"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("data.purpose").type(JsonFieldType.STRING).description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? ??????")
    void searchMyList() throws Exception {
        this.mockMvc.perform(
                        get("/api/items/my-list")
                                .header("authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("item-controller-test/search-my-list",
                        queryParameters(
                                parameterWithName("page").description("????????? ??????").optional(),
                                parameterWithName("size").description("????????? ??????").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data[].item_id").type(JsonFieldType.NUMBER).description("?????? ID").optional(),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("?????? ??????").optional(),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("?????? ????????????").optional(),
                                fieldWithPath("data[].img_url").type(JsonFieldType.STRING).description("?????? ????????? URL").optional(),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("?????? ????????????").optional(),
                                fieldWithPath("data[].trade_status").type(JsonFieldType.STRING).description("?????? ????????????").optional()
                        )));
    }

    @Test
    @DisplayName("?????? ?????? ???????????? ??????")
    void updateTradeStatus() throws Exception {
        Item item = itemRepository.findByName("????????? ??????");

        String itemUpdateTradeStatusRequest = objectMapper.writeValueAsString(
                new ItemUpdateTradeStatusRequest(item.getId(), ItemTradeStatus.AFTER));
        this.mockMvc.perform(
                        put("/api/items/my-list/trade-status")
                                .header("authorization", accessToken)
                                .content(itemUpdateTradeStatusRequest)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("item-controller-test/update-trade-status",
                        requestFields(
                                fieldWithPath("item_id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("trade_status").type(JsonFieldType.STRING).description("?????? ????????????")
                                        .attributes(RestDocsConfig.field("constraints",
                                                String.format("%s,%s ??? ??? 1", ItemTradeStatus.BEFORE, ItemTradeStatus.AFTER)))
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.item_id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("data.trade_status").type(JsonFieldType.STRING).description("?????? ????????????")
                                        .attributes(RestDocsConfig.field("constraints",
                                                String.format("%s,%s ??? ??? 1", ItemTradeStatus.BEFORE, ItemTradeStatus.AFTER)))
                        )));
    }
}