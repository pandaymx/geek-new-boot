package com.ppmb.user.exception;

import com.ppmb.core.exception.BusinessException;

public class UserAccountDisabledException extends BusinessException {
    public UserAccountDisabledException() {
        super(UserErrorCode.USER_ACCOUNT_DISABLED);
    }
}
