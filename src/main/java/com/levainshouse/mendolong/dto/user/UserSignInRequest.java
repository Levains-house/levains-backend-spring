package com.levainshouse.mendolong.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSignInRequest implements Serializable {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private final String username;

    @NotBlank(message = "카카오톡 오픈 채팅방 URL을 입력해주세요.")
    @Pattern(regexp = "https://open.kakao.com/[a-zA-Z0-9_.]*", message = "카카오톡 오픈 채팅방 URL을 올바른 형식으로 입력해주세요.")
    private final String kakaoTalkChattingUrl;

    @NotNull(message = "사용자 유형을 입력해주세요.")
    private final UserRole role;
}
