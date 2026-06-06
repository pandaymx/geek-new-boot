package com.ppmb.menu.exception;

import com.ppmb.core.exception.BusinessException;

public class MenuNotFoundException extends BusinessException {
    public MenuNotFoundException() {
        super(MenuErrorCode.MENU_NOT_FOUND);
    }
}
