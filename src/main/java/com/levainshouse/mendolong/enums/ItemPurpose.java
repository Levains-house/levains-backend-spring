package com.levainshouse.mendolong.enums;

public enum ItemPurpose {
    SHARE("나눔"),
    WANT("원함");

    private final String label;

    ItemPurpose(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
