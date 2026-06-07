package com.ppmb.role.exception;

import com.ppmb.core.exception.ErrorCode;

public enum RoleErrorCode implements ErrorCode {
    ROLE_NOT_FOUND(30001, "Role not found"),
    ROLE_CODE_ALREADY_EXISTS(30002, "Role code already exists"),
    ROLE_ASSIGNMENT_LIMIT(30003, "Role assignment limit exceeded"),
    ROLE_IN_USE(30004, "Role is currently in use and cannot be deleted");

    private final int code;
    private final String message;

    RoleErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
