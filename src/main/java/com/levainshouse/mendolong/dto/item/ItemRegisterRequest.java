package com.levainshouse.mendolong.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRegisterRequest implements Serializable {

    @NotBlank(message = "물건 이름을 입력해주세요.")
    private final String name;

    @NotBlank(message = "물건 상세정보를 입력해주세요.")
    private final String description;

    @NotNull(message = "물건 카테고리를 입력해주세요.")
    private final ItemCategory category;

    @NotNull(message = "물건 나눔목적을 입력해주세요.")
    private final ItemPurpose purpose;
}
