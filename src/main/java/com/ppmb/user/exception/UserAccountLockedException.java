package com.ppmb.user.exception;

import com.ppmb.core.exception.BusinessException;

public class UserAccountLockedException extends BusinessException {
    public UserAccountLockedException() {
        super(UserErrorCode.USER_ACCOUNT_LOCKED);
    }
}
