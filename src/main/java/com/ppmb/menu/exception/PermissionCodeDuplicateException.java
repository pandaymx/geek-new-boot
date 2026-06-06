package com.ppmb.menu.exception;

import com.ppmb.core.exception.BusinessException;

public class PermissionCodeDuplicateException extends BusinessException {
    public PermissionCodeDuplicateException() {
        super(MenuErrorCode.PERMISSION_CODE_DUPLICATE);
    }
}
