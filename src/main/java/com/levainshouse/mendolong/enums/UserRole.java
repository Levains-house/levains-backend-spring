package com.levainshouse.mendolong.enums;

public enum UserRole {
    LOCAL("제주도민"),
    TRAVEL("여행객");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
