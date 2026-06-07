package com.ppmb.dept.exception;

import com.ppmb.core.exception.BusinessException;

public class DeptNotFoundException extends BusinessException {
    public DeptNotFoundException() {
        super(DeptErrorCode.DEPT_NOT_FOUND);
    }
}
