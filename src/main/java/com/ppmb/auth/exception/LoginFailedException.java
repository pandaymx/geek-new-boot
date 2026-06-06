package com.ppmb.auth.exception;

import com.ppmb.core.exception.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super(AuthErrorCode.LOGIN_FAILED);
    }

    public LoginFailedException(Throwable cause) {
        super(AuthErrorCode.LOGIN_FAILED, cause);
    }
}
