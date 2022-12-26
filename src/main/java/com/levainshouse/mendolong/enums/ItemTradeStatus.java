package com.levainshouse.mendolong.enums;

public enum ItemTradeStatus {
    BEFORE("거래 전"),
    AFTER("거래 완료");

    private final String label;

    ItemTradeStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
