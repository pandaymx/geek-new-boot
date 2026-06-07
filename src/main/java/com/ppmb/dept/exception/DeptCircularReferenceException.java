package com.ppmb.dept.exception;

import com.ppmb.core.exception.BusinessException;

public class DeptCircularReferenceException extends BusinessException {
    public DeptCircularReferenceException() {
        super(DeptErrorCode.DEPT_CIRCULAR_REFERENCE);
    }
}
