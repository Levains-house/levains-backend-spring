package com.levainshouse.mendolong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levainshouse.mendolong.RestDocsConfig;
import com.levainshouse.mendolong.dto.address.AddressRegisterRequest;
import com.levainshouse.mendolong.dto.user.UserSignInRequest;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.UserRole;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@Transactional
class UserControllerTest {

    private String accessToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        User user = userService.signIn(
                new UserSignInRequest("?????? ?????????", "https://open.kakao.com/test", UserRole.TRAVEL));
        this.accessToken = tokenService.issue(user);
    }

    @Test
    @DisplayName("????????? ?????????")
    void signIn() throws Exception {
        String signInRequest = objectMapper.writeValueAsString(
                new UserSignInRequest("?????????1", "https://open.kakao.com/test", UserRole.TRAVEL));
        this.mockMvc.perform(
                        post("/api/users/sign-in")
                                .content(signInRequest)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("user-controller-test/sign-in",
                        requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("kakao_talk_chatting_url").type(JsonFieldType.STRING)
                                        .description("????????? ???????????? ?????? ????????? URL")
                                        .attributes(Attributes.key("constraints")
                                                .value(String.format("%s ???????????? ??????", "https://open.kakao.com/"))),
                                fieldWithPath("role").type(JsonFieldType.STRING).description("????????? ??????")
                                        .attributes(Attributes.key("constraints")
                                                .value(String.format("%s,%s ??? ??? 1", UserRole.TRAVEL, UserRole.LOCAL)))
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.user_id").type(JsonFieldType.NUMBER).description("????????? ID"),
                                fieldWithPath("data.username").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("data.kakao_talk_chatting_url").type(JsonFieldType.STRING)
                                        .description("????????? ???????????? ?????? ????????? URL"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("????????? ??????")
                        )));
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void signInAddress() throws Exception {
        List<AddressRegisterRequest> addressRegisterRequests = new ArrayList<>();
        addressRegisterRequests.add(new AddressRegisterRequest(123.123, 234.234));
        addressRegisterRequests.add(new AddressRegisterRequest(345.345, 456.456));
        String registerRequests = objectMapper.writeValueAsString(addressRegisterRequests);

        this.mockMvc.perform(
                        post("/api/users/sign-in/address")
                                .header("authorization", this.accessToken)
                                .content(registerRequests)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document("user-controller-test/sign-in-address",
                        requestFields(
                                fieldWithPath("[].latitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("[].longitude").type(JsonFieldType.NUMBER).description("??????")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data[].address_id").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("data[].latitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data[].longitude").type(JsonFieldType.NUMBER).description("??????")
                        )));
    }
}