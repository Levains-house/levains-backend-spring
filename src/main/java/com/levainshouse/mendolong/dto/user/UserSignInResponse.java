package com.levainshouse.mendolong.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.UserRole;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSignInResponse implements Serializable {

    private final Long userId;
    private final String username;
    private final String kakaoTalkChattingUrl;
    private final UserRole role;

    public UserSignInResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.kakaoTalkChattingUrl = user.getKakaoTalkChattingUrl();
        this.role = user.getRole();
    }
}
