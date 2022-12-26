package com.levainshouse.mendolong.repository;

import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.enums.ItemTradeStatus;
import com.levainshouse.mendolong.repository.condition.ItemRecommendExperienceSearchCondition;
import com.levainshouse.mendolong.repository.condition.ItemRecommendSearchCondition;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.levainshouse.mendolong.entity.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemCustomRepositoryImpl implements ItemCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> searchRecommendItems(ItemRecommendSearchCondition condition, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(item)
                .where(item.user.in(condition.getUsers()))
                .where(item.purpose.eq(condition.getPurpose()))
                .where(item.category.in(condition.getCategories()))
                .where(item.tradeStatus.eq(ItemTradeStatus.BEFORE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Item> searchRecommendExperienceItems(ItemRecommendExperienceSearchCondition condition, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(item)
                .where(item.user.in(condition.getUsers()))
                .where(item.purpose.eq(condition.getPurpose()))
                .where(item.category.eq(ItemCategory.EXPERIENCE))
                .where(item.tradeStatus.eq(ItemTradeStatus.BEFORE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Item> searchMyItems(User user, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(item)
                .where(item.user.eq(user))
                .where(item.purpose.eq(ItemPurpose.SHARE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
