package com.ppmb.core.presentation.advice;

import com.ppmb.core.presentation.response.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

/** Global response advice to wrap all API responses with Result<T>. */
@RestControllerAdvice(basePackages = "com.ppmb")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public GlobalResponseAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(
            MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // If the return type is already a Result, no need to wrap again.
        return !Result.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // If the return type is String, Spring will use StringHttpMessageConverter which cannot
        // process Result.
        // We must manually serialize it to JSON String.
        if (String.class.isAssignableFrom(returnType.getParameterType())) {
            try {
                return objectMapper.writeValueAsString(Result.success(body));
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize response", e);
            }
        }

        return Result.success(body);
    }
}
