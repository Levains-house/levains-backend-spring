package com.levainshouse.mendolong.enums;

public enum ItemCategory {
    CLOTH("의류"),
    THINGS("잡화"),
    BOOK("도서"),
    LIVE_THINGS("생활용품"),
    BABY_THINGS("유아용품"),
    EXPERIENCE("체험");

    private final String label;

    ItemCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
