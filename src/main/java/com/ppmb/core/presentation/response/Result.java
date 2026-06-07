package com.ppmb.core.presentation.response;

import org.slf4j.MDC;

/**
 * Unified API Response Wrapper.
 *
 * @param <T> the type of the payload data
 */
public record Result<T>(int code, String message, T data, long timestamp, String traceId) {
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data, System.currentTimeMillis(), MDC.get("traceId"));
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis(), MDC.get("traceId"));
    }
}
