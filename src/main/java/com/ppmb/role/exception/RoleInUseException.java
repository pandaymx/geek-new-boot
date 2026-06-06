package com.ppmb.role.exception;

import com.ppmb.core.exception.BusinessException;

public class RoleInUseException extends BusinessException {
    public RoleInUseException() {
        super(RoleErrorCode.ROLE_IN_USE);
    }
}
