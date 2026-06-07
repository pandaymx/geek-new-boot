package com.ppmb.user.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.model.AuditInfo;
import com.ppmb.core.domain.base.model.TenantId;
import com.ppmb.user.domain.model.UserId;
import com.ppmb.user.domain.model.UserStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_user")
public class User extends AbstractEntity<UserId> implements Auditable {

    @Id private Long id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "status")
    private UserStatus status;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "tenant_id", nullable = false, length = 64))
    private TenantId tenantId;

    @Embedded private AuditInfo auditInfo;

    protected User() {
        // JPA Required
    }

    @Override
    public UserId getId() {
        return id != null ? new UserId(id) : null;
    }

    public void setId(UserId id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }
}
