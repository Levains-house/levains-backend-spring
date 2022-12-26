package com.levainshouse.mendolong.repository.condition;

import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class ItemRecommendSearchCondition {

    private final List<User> users;
    private final ItemPurpose purpose;
    private final List<ItemCategory> categories;
}
