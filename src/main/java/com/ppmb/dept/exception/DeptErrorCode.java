package com.ppmb.dept.exception;

import com.ppmb.core.exception.ErrorCode;

public enum DeptErrorCode implements ErrorCode {
    DEPT_NOT_FOUND(50001, "Department not found"),
    DEPT_CIRCULAR_REFERENCE(50002, "Department circular reference detected"),
    DEPT_HAS_CHILDREN(50003, "Please delete child departments first");

    private final int code;
    private final String message;

    DeptErrorCode(int code, String message) {
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
