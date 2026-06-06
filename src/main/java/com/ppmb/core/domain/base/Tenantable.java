package com.ppmb.core.domain.base;

import com.ppmb.core.domain.base.model.TenantId;

public interface Tenantable {

    /**
     * Get the tenant ID for this entity.
     *
     * @return the tenant ID
     */
    TenantId getTenantId();

    /**
     * Set the tenant ID for this entity.
     *
     * @param tenantId the new tenant ID
     */
    void setTenantId(TenantId tenantId);
}
