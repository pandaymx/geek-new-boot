package com.ppmb.dept.domain.model;

public enum DeptStatus {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用");

    private final int code;
    private final String description;

    DeptStatus(int code, String description) {
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
