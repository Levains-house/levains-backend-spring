package com.levainshouse.mendolong.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AddressRequestVo implements Serializable {

    @NotNull(message = "위도를 입력해주세요.")
    private final Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private final Double longitude;
}
