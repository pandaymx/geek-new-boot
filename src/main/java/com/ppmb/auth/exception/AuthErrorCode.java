package com.ppmb.auth.exception;

import com.ppmb.core.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    LOGIN_FAILED(20001, "Username or password error");

    private final int code;
    private final String message;

    AuthErrorCode(int code, String message) {
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
