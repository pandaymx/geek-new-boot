package com.ppmb.core.domain.base;

import com.ppmb.core.domain.base.model.AuditInfo;

public interface Auditable {

    /**
     * Get the audit info for this entity.
     *
     * @return the audit info
     */
    AuditInfo getAuditInfo();

    /**
     * Set the audit info for this entity.
     *
     * @param auditInfo the new audit info
     */
    void setAuditInfo(AuditInfo auditInfo);
}
