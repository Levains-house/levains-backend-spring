package com.levainshouse.mendolong.exceptionhandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponse {

    private final String code;
    private final String message;
}
