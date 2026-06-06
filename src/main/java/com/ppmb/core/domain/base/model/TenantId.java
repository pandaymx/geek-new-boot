package com.ppmb.core.domain.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record TenantId(@Column(name = "tenant_id", nullable = false, length = 64) String value) {
    public TenantId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TenantId value cannot be null or blank");
        }
    }
}
