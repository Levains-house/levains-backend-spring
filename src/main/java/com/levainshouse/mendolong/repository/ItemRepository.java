package com.levainshouse.mendolong.repository;

import com.levainshouse.mendolong.entity.Item;
import com.levainshouse.mendolong.entity.User;
import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByName(String name);

    @Query("select i from Item i where i.user in (:users) and i.purpose = :purpose")
    List<Item> searchItemsByUsersAndPurpose(@Param("users") List<User> users, @Param("purpose") ItemPurpose purpose);

    @Query("select i.category from Item i where i.user = :user and i.purpose = :purpose")
    List<ItemCategory> findCategoriesByUserAndPurpose(@Param("user") User user, @Param("purpose") ItemPurpose purpose);
}
