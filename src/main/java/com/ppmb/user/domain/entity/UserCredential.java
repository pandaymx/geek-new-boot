package com.ppmb.user.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.model.AuditInfo;
import com.ppmb.core.domain.base.model.TenantId;
import com.ppmb.user.domain.model.CredentialId;
import com.ppmb.user.domain.model.IdentityType;
import com.ppmb.user.domain.model.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_user_credential")
public class UserCredential extends AbstractEntity<CredentialId> implements Auditable {

    @Id private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "identity_type", length = 30, nullable = false)
    private IdentityType identityType;

    @Column(name = "identifier", length = 100, nullable = false)
    private String identifier;

    @Column(name = "credential", length = 500)
    private String credential;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "tenant_id", nullable = false, length = 64))
    private TenantId tenantId;

    @Embedded private AuditInfo auditInfo;

    protected UserCredential() {
        // JPA Required
    }

    @Override
    public CredentialId getId() {
        return id != null ? new CredentialId(id) : null;
    }

    public void setId(CredentialId id) {
        this.id = id != null ? id.value() : null;
    }

    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public UserId getUserId() {
        return userId != null ? new UserId(userId) : null;
    }

    public void setUserId(UserId userId) {
        this.userId = userId != null ? userId.value() : null;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }
}
