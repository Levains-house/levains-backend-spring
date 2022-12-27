package com.levainshouse.mendolong.utils;

import jakarta.validation.constraints.NotBlank;

public record ErrorRequest(
        @NotBlank(message = "값을 입력해주세요.") String test) {

}
