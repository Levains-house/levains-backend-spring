package com.levainshouse.mendolong.constant;

public interface JwtTokenProperties {

    String JWT_PREFIX = "Bearer ";
    String JWT_SECRET_KEY = "secret key";
    Long TOKEN_EXPIRED_TIME = 60000L * 60 * 24 * 31;
}
