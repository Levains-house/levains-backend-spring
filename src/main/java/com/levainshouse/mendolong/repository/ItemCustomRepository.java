package com.levainshouse.mendolong.repository;

import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.repository.condition.ItemRecommendExperienceSearchCondition;
import com.levainshouse.mendolong.repository.condition.ItemRecommendSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemCustomRepository {

    List<Item> searchRecommendItems(ItemRecommendSearchCondition condition, Pageable pageable);
    List<Item> searchRecommendExperienceItems(ItemRecommendExperienceSearchCondition condition, Pageable pageable);

    List<Item> searchMyItems(User user, Pageable pageable);
}
