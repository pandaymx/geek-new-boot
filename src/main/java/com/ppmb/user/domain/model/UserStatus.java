package com.ppmb.user.domain.model;

public enum UserStatus {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
