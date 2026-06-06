package com.ppmb.core.presentation.advice;

import com.ppmb.core.exception.BusinessException;
import com.ppmb.core.exception.ErrorCode;
import com.ppmb.core.exception.SystemErrorCode;
import com.ppmb.core.presentation.response.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global exception handler for handling all unhandled exceptions. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /** Handle business exceptions. */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        String message = resolveMessage(errorCode);
        log.warn("Business exception occurred: [{}] {}", errorCode.code(), message, e);
        return Result.error(errorCode.code(), message);
    }

    /** Handle validation exceptions (MethodArgumentNotValidException). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation exception occurred: {}", message);
        return Result.error(SystemErrorCode.BAD_REQUEST.code(), message);
    }

    /** Handle validation exceptions (ConstraintViolationException). */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("Constraint violation exception occurred: {}", message);
        return Result.error(SystemErrorCode.BAD_REQUEST.code(), message);
    }

    /** Handle validation exceptions (BindException). */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Bind exception occurred: {}", message);
        return Result.error(SystemErrorCode.BAD_REQUEST.code(), message);
    }

    /** Handle JSON parsing or malformed HTTP message exceptions. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("HTTP message not readable exception occurred: {}", e.getMessage());
        String message = resolveMessage(SystemErrorCode.BAD_REQUEST);
        return Result.error(SystemErrorCode.BAD_REQUEST.code(), message);
    }

    /** Handle generic exceptions (fallback). */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unhandled system exception occurred: {}", e.getMessage(), e);
        String message = resolveMessage(SystemErrorCode.SYSTEM_ERROR);
        return Result.error(SystemErrorCode.SYSTEM_ERROR.code(), message);
    }

    /** Helper method to resolve ErrorCode message via MessageSource. */
    private String resolveMessage(ErrorCode errorCode) {
        Locale locale = LocaleContextHolder.getLocale();
        String defaultMessage = errorCode.message();

        // 1. Try to resolve by numeric code e.g. error.10001
        String codeKey = "error." + errorCode.code();
        String message = messageSource.getMessage(codeKey, null, null, locale);
        if (message != null) {
            return message;
        }

        // 2. Try to resolve by enum name if it's an enum, e.g. error.USER_NOT_FOUND
        if (errorCode instanceof Enum) {
            String enumKey = "error." + ((Enum<?>) errorCode).name();
            message = messageSource.getMessage(enumKey, null, null, locale);
            if (message != null) {
                return message;
            }
        }

        return defaultMessage;
    }
}
