package com.levainshouse.mendolong.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRegisterResponse implements Serializable {

    private final Long itemId;
    private final String name;
    private final String description;
    private final String imgUrl;
    private final ItemCategory category;
    private final ItemPurpose purpose;

    public ItemRegisterResponse(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.imgUrl = item.getImgUrl();
        this.category = item.getCategory();
        this.purpose = item.getPurpose();
    }
}
