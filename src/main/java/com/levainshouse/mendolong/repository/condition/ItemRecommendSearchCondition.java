package com.levainshouse.mendolong.repository.condition;

import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;

import java.util.List;

public record ItemRecommendSearchCondition(
        List<User> users,
        ItemPurpose purpose,
        List<ItemCategory> categories) {

}
