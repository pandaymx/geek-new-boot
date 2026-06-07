package com.ppmb.core.presentation;

import org.slf4j.MDC;

public record Result<T>(int code, String message, T data, String traceId) {

    private static final String TRACE_ID_KEY = "traceId";

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data, MDC.get(TRACE_ID_KEY));
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message, null, MDC.get(TRACE_ID_KEY));
    }
}
