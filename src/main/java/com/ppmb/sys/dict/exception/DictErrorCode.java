package com.ppmb.sys.dict.exception;

import com.ppmb.core.exception.ErrorCode;

public enum DictErrorCode implements ErrorCode {
    DICT_TYPE_NOT_FOUND(40401, "Dictionary type not found"),
    DICT_TYPE_ALREADY_EXISTS(40001, "Dictionary type already exists"),
    DICT_DATA_NOT_FOUND(40402, "Dictionary data not found"),
    DICT_DATA_ALREADY_EXISTS(
            40002, "Dictionary data with the same value already exists in this type");

    private final int code;
    private final String message;

    DictErrorCode(int code, String message) {
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
