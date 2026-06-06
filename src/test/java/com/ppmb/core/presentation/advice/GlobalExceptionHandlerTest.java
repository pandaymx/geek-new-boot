package com.ppmb.core.presentation.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ppmb.core.exception.BusinessException;
import com.ppmb.core.presentation.response.Result;
import com.ppmb.user.exception.UserNotFoundException;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

class GlobalExceptionHandlerTest {

    private MessageSource messageSource;
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        exceptionHandler = new GlobalExceptionHandler(messageSource);
    }

    @Test
    void testHandleBusinessExceptionWithChineseLocale() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
        BusinessException exception = new UserNotFoundException(); // code 10001

        when(messageSource.getMessage(eq("error.10001"), any(), any(), eq(Locale.SIMPLIFIED_CHINESE)))
                .thenReturn("用户不存在");

        // Act
        Result<Void> result = exceptionHandler.handleBusinessException(exception);

        // Assert
        assertEquals(10001, result.code());
        assertEquals("用户不存在", result.message());
    }

    @Test
    void testHandleBusinessExceptionWithEnglishLocale() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        BusinessException exception = new UserNotFoundException(); // code 10001

        when(messageSource.getMessage(eq("error.10001"), any(), any(), eq(Locale.US)))
                .thenReturn("User not found");

        // Act
        Result<Void> result = exceptionHandler.handleBusinessException(exception);

        // Assert
        assertEquals(10001, result.code());
        assertEquals("User not found", result.message());
    }

    @Test
    void testHandleBusinessExceptionFallback() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.US);
        BusinessException exception = new UserNotFoundException(); // code 10001

        // Return null to simulate missing translation, should fallback to default message
        when(messageSource.getMessage(eq("error.10001"), any(), any(), eq(Locale.US)))
                .thenReturn(null);
        when(messageSource.getMessage(eq("error.USER_NOT_FOUND"), any(), any(), eq(Locale.US)))
                .thenReturn(null);

        // Act
        Result<Void> result = exceptionHandler.handleBusinessException(exception);

        // Assert
        assertEquals(10001, result.code());
        assertEquals("User not found", result.message()); // default error message
    }

    @Test
    void testHandleException() {
        // Arrange
        LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
        Exception exception = new RuntimeException("Generic system error");

        when(messageSource.getMessage(eq("error.500"), any(), any(), eq(Locale.SIMPLIFIED_CHINESE)))
                .thenReturn("系统内部错误");

        // Act
        Result<Void> result = exceptionHandler.handleException(exception);

        // Assert
        assertEquals(500, result.code());
        assertEquals("系统内部错误", result.message());
    }
}
