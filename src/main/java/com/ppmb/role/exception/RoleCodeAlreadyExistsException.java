package com.ppmb.role.exception;

import com.ppmb.core.exception.BusinessException;

public class RoleCodeAlreadyExistsException extends BusinessException {
    public RoleCodeAlreadyExistsException() {
        super(RoleErrorCode.ROLE_CODE_ALREADY_EXISTS);
    }
}
