package com.ppmb.dept.domain.model;

import com.ppmb.core.domain.base.model.DomainId;

public record DeptId(Long value) implements DomainId {
    public DeptId {
        if (value == null) {
            throw new IllegalArgumentException("DeptId value cannot be null");
        }
    }
}
