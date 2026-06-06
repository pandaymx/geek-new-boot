package com.ppmb.user.exception;

import com.ppmb.core.exception.BusinessException;

public class BadCredentialsException extends BusinessException {
    public BadCredentialsException() {
        super(UserErrorCode.BAD_CREDENTIALS);
    }
}
