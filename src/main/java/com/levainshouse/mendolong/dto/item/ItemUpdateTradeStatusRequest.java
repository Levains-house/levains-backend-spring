package com.levainshouse.mendolong.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.enums.ItemTradeStatus;
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
public class ItemUpdateTradeStatusRequest implements Serializable {

    @NotNull(message = "물건 ID를 입력해주세요.")
    private final Long itemId;

    @NotBlank(message = "물건 거래 상태를 입력해주세요.")
    private final ItemTradeStatus tradeStatus;
}
