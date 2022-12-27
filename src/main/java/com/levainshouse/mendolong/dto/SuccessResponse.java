package com.levainshouse.mendolong.dto;

public record SuccessResponse(
        String code,
        String message,
        Object data) {

}
