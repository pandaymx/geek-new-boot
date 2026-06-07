package com.ppmb.core.domain.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class TenantId {

    @Column(name = "tenant_id", nullable = false, length = 64)
    private String value;

    protected TenantId() {
        // JPA requires a protected no-arg constructor
    }

    public TenantId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TenantId value cannot be null or blank");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(value, tenantId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TenantId[" + "value=" + value + ']';
    }
}
