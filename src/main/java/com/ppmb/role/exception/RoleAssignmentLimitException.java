package com.ppmb.role.exception;

import com.ppmb.core.exception.BusinessException;

public class RoleAssignmentLimitException extends BusinessException {
    public RoleAssignmentLimitException() {
        super(RoleErrorCode.ROLE_ASSIGNMENT_LIMIT);
    }
}
