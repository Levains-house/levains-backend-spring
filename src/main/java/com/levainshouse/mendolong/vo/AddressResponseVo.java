package com.levainshouse.mendolong.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AddressResponseVo implements Serializable {

    private final Long addressId;
    private final Double latitude;
    private final Double longitude;
}
