package com.ppmb.menu.exception;

import com.ppmb.core.exception.ErrorCode;

public enum MenuErrorCode implements ErrorCode {
    MENU_NOT_FOUND(40001, "Menu not found"),
    MENU_CIRCULAR_REFERENCE(40002, "Menu circular reference detected"),
    MENU_HAS_CHILDREN(40003, "Please delete child nodes first"),
    PERMISSION_CODE_DUPLICATE(40004, "Permission code duplicate");

    private final int code;
    private final String message;

    MenuErrorCode(int code, String message) {
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
