package com.ppmb.menu.exception;

import com.ppmb.core.exception.BusinessException;

public class MenuHasChildrenException extends BusinessException {
    public MenuHasChildrenException() {
        super(MenuErrorCode.MENU_HAS_CHILDREN);
    }
}
