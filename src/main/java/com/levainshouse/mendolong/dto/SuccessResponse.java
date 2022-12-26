package com.levainshouse.mendolong.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SuccessResponse {

    private final String code;
    private final String message;
    private final Object data;
}
