package com.levainshouse.mendolong.repository.condition;

import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemPurpose;
import lombok.Data;

import java.util.List;

@Data
public class ItemRecommendExperienceSearchCondition {

    private final List<User> users;
    private final ItemPurpose purpose;
}
