package com.ppmb.core.exception;

/** The base interface for all error codes. */
public interface ErrorCode {
    /**
     * @return the error code integer value
     */
    int code();

    /**
     * @return the error message
     */
    String message();
}
