package com.ppmb.application.service;

/** 自定义领域异常，用于表示身份令牌无效（例如：过期、签名错误或结构损坏）。 这样可以防止底层加密库的异常（如 JJWT 或 Auth0 的异常）直接抛到上层业务逻辑中，完美实现技术解耦。 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
