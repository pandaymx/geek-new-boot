package com.ppmb.user.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.model.TenantId;
import com.ppmb.user.domain.model.IdentityType;
import com.ppmb.user.domain.model.LoginLogId;
import com.ppmb.user.domain.model.LoginStatus;
import com.ppmb.user.domain.model.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "sys_user_login_log")
public class UserLoginLog extends AbstractEntity<LoginLogId> {

    @Id private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_account", length = 100)
    private String loginAccount;

    @Column(name = "identity_type", length = 30)
    private IdentityType identityType;

    @Column(name = "login_time", nullable = false)
    private Instant loginTime;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "browser", length = 50)
    private String browser;

    @Column(name = "os", length = 50)
    private String os;

    @Column(name = "status")
    private LoginStatus status;

    @Column(name = "message", length = 255)
    private String message;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "tenant_id", nullable = false, length = 64))
    private TenantId tenantId;

    protected UserLoginLog() {
        // JPA Required
    }

    @Override
    public LoginLogId getId() {
        return id != null ? new LoginLogId(id) : null;
    }

    public void setId(LoginLogId id) {
        this.id = id != null ? id.value() : null;
    }

    public UserId getUserId() {
        return userId != null ? new UserId(userId) : null;
    }

    public void setUserId(UserId userId) {
        this.userId = userId != null ? userId.value() : null;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public Instant getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }
}
