package com.ppmb.core.presentation.advice;

import com.ppmb.core.exception.BusinessException;
import com.ppmb.core.exception.SystemErrorCode;
import com.ppmb.core.presentation.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global exception handler for handling all unhandled exceptions. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Handle business exceptions. */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn(
                "Business exception occurred: [{}] {}", e.getErrorCode().code(), e.getMessage(), e);
        return Result.error(e.getErrorCode().code(), e.getMessage());
    }

    /** Handle generic exceptions (fallback). */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unhandled system exception occurred: {}", e.getMessage(), e);
        return Result.error(
                SystemErrorCode.SYSTEM_ERROR.code(), SystemErrorCode.SYSTEM_ERROR.message());
    }
}
