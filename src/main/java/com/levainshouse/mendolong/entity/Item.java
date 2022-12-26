package com.levainshouse.mendolong.entity;

import com.levainshouse.mendolong.enums.ItemCategory;
import com.levainshouse.mendolong.enums.ItemPurpose;
import com.levainshouse.mendolong.enums.ItemTradeStatus;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "items")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Item {

    @Column(name = "item_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name", nullable = false)
    private final String name;

    @Column(name = "description", nullable = false)
    private final String description;

    @Column(name = "img_name")
    private final String imgName;

    @Column(name = "img_url")
    private final String imgUrl;

    @Column(name = "purpose", nullable = false,
            columnDefinition = "VARCHAR(255) CHECK (purpose IN ('SHARE','WANT'))")
    @Enumerated(EnumType.STRING)
    private final ItemPurpose purpose;

    @Column(name = "category", nullable = false,
            columnDefinition = "VARCHAR(255) CHECK (category IN ('CLOTH','THINGS','BOOK','LIVE_THINGS','BABY_THINGS','EXPERIENCE'))")
    @Enumerated(EnumType.STRING)
    private final ItemCategory category;

    @Column(name = "trade_status", nullable = false,
            columnDefinition = "VARCHAR(255) CHECK (trade_status IN ('BEFORE','AFTER'))")
    @Enumerated(EnumType.STRING)
    private ItemTradeStatus tradeStatus;

    public Item updateTradeStatus(ItemTradeStatus tradeStatus){
        this.tradeStatus = tradeStatus;
        return this;
    }
}
