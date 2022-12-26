package com.levainshouse.mendolong.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemWantResponseVo {

    private final Long itemId;
    private final String name;
    private final String description;
    private final String imgUrl;
    private final ItemPurpose purpose;
    private final ItemCategory category;

    public ItemWantResponseVo(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.imgUrl = item.getImgUrl();
        this.purpose = item.getPurpose();
        this.category = item.getCategory();
    }
}
