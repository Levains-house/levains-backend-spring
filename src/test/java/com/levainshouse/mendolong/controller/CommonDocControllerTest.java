package com.levainshouse.mendolong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levainshouse.mendolong.RestDocsConfig;
import com.levainshouse.mendolong.utils.ErrorRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
class CommonDocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("에러 상태코드")
    void error() throws Exception {
        ErrorRequest errorRequest = new ErrorRequest("");

        this.mockMvc.perform(
                        post("/api/error")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(errorRequest))
                )
                .andExpect(status().isBadRequest())
                .andDo(
                        document("common-doc-controller-test/error",
                                responseFields(
                                        fieldWithPath("code").description("에러 코드"),
                                        fieldWithPath("message").description("에러 메시지")
                                )
                        )
                )
        ;
    }
}