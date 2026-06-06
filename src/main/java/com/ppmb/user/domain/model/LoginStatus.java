package com.ppmb.user.domain.model;

public enum LoginStatus {
    SUCCESS(0, "成功"),
    FAILURE(1, "失败");

    private final int code;
    private final String description;

    LoginStatus(int code, String description) {
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
