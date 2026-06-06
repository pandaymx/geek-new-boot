package com.ppmb.user.exception;

import com.ppmb.core.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(10001, "User not found"),
    USERNAME_ALREADY_EXISTS(10002, "Username already exists"),
    EMAIL_ALREADY_EXISTS(10003, "Email already exists"),
    USER_ACCOUNT_LOCKED(10004, "User account is locked"),
    USER_ACCOUNT_DISABLED(10005, "User account is disabled"),
    BAD_CREDENTIALS(10006, "Bad credentials");

    private final int code;
    private final String message;

    UserErrorCode(int code, String message) {
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
