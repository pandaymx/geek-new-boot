package com.ppmb.menu.exception;

import com.ppmb.core.exception.BusinessException;

public class MenuCircularReferenceException extends BusinessException {
    public MenuCircularReferenceException() {
        super(MenuErrorCode.MENU_CIRCULAR_REFERENCE);
    }
}
