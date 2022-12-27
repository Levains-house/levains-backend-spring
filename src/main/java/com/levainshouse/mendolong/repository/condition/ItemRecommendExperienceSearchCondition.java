package com.levainshouse.mendolong.repository.condition;

import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemPurpose;

import java.util.List;

public record ItemRecommendExperienceSearchCondition(
        List<User> users,
        ItemPurpose purpose) {

}
