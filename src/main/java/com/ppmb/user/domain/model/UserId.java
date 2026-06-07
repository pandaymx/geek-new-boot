package com.ppmb.user.domain.model;

import com.ppmb.core.domain.base.model.DomainId;

public record UserId(Long value) implements DomainId {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("UserId value cannot be null");
        }
    }
}
