package com.levainshouse.mendolong.utils;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ErrorRequest {

    @NotBlank(message = "값을 입력해주세요.")
    private final String test;
}
