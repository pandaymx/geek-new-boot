package com.ppmb.user.domain.model;

import com.ppmb.core.domain.base.model.DomainId;
import java.io.Serializable;

public record LoginLogId(Long value) implements DomainId, Serializable {
    public LoginLogId {
        if (value == null) {
            throw new IllegalArgumentException("LoginLogId value cannot be null");
        }
    }
}
