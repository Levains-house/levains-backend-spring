package com.levainshouse.mendolong.dto.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.vo.AddressRequestVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRegisterRequest implements Serializable {

    @NotNull(message = "위도를 입력해주세요.")
    @NotEmpty(message = "위도를 입력해주세요.")
    private final Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    @NotEmpty(message = "경도를 입력해주세요.")
    private final Double longitude;
}

