package com.levainshouse.mendolong.controller;

import com.levainshouse.mendolong.constant.WebProperties;
import com.levainshouse.mendolong.dto.SuccessResponse;
import com.levainshouse.mendolong.utils.ErrorRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error")
public class CommonDocController {

    @PostMapping
    public ResponseEntity<SuccessResponse> error(@Valid @RequestBody ErrorRequest errorRequest){
        return ResponseEntity.ok()
                .body(new SuccessResponse(
                        HttpStatus.OK.name(),
                        WebProperties.SUCCESS_RESPONSE_MESSAGE,
                        ""
                ));
    }
}
