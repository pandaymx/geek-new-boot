package com.ppmb.dept.exception;

import com.ppmb.core.exception.BusinessException;

public class DeptHasChildrenException extends BusinessException {
    public DeptHasChildrenException() {
        super(DeptErrorCode.DEPT_HAS_CHILDREN);
    }
}
