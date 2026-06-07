package com.ppmb.user.domain.model;

import com.ppmb.core.domain.base.model.DomainId;

public record LoginLogId(Long value) implements DomainId {
    public LoginLogId {
        if (value == null) {
            throw new IllegalArgumentException("LoginLogId value cannot be null");
        }
    }
}
