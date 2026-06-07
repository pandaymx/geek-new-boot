package com.ppmb.role.exception;

import com.ppmb.core.exception.BusinessException;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException() {
        super(RoleErrorCode.ROLE_NOT_FOUND);
    }
}
