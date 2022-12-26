package com.levainshouse.mendolong.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.levainshouse.mendolong.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.levainshouse.mendolong.constant.JwtTokenProperties.*;

@Slf4j
@Service
public class TokenService {

    private static final String TAG = "TOKEN_SERVICE=%s";

    public String issue(User user){
        log.debug(String.format(TAG, "Issue access token"));
        return JWT_PREFIX + JWT.create()
                .withSubject(user.getId().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME))
                .withClaim("username", user.getUsername())
                .withClaim("kakao_talk_chatting_url", user.getKakaoTalkChattingUrl())
                .withClaim("role", user.getRole().toString())
                .sign(Algorithm.HMAC512(JWT_SECRET_KEY));
    }

    public Long verify(String accessToken){
        log.debug(String.format(TAG, "Verify access token"));
        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC512(JWT_SECRET_KEY))
                .build()
                .verify(accessToken.substring(JWT_PREFIX.length()));

        return Long.parseLong(decodedJWT.getSubject());
    }
}
