package com.levainshouse.mendolong.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.levainshouse.mendolong.vo.ItemExperienceRecommendResponseVo;
import com.levainshouse.mendolong.vo.ItemRecommendResponseVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemGetRecommendResponse {

    private final List<ItemRecommendResponseVo> recommendItems;
    private final List<ItemExperienceRecommendResponseVo> experienceRecommendItems;
}
