package com.ppmb.user.exception;

import com.ppmb.core.exception.BusinessException;

public class UsernameAlreadyExistsException extends BusinessException {
    public UsernameAlreadyExistsException() {
        super(UserErrorCode.USERNAME_ALREADY_EXISTS);
    }
}
