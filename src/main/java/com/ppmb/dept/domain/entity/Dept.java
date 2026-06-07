package com.ppmb.dept.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.Tenantable;
import com.ppmb.core.domain.base.Treeable;
import com.ppmb.core.domain.base.model.AuditInfo;
import com.ppmb.core.domain.base.model.TenantId;
import com.ppmb.dept.domain.model.DeptId;
import com.ppmb.dept.domain.model.DeptStatus;
import com.ppmb.user.domain.model.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_dept")
public class Dept extends AbstractEntity<DeptId>
        implements Auditable, Tenantable, Treeable<DeptId> {

    @Id private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "path", length = 255)
    private String path;

    @Column(name = "dept_name", length = 50)
    private String deptName;

    @Column(name = "dept_code", length = 50)
    private String deptCode;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "leader_user_id")
    private Long leaderUserId;

    @Column(name = "status")
    private DeptStatus status;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "tenant_id", nullable = false, length = 64))
    private TenantId tenantId;

    @Embedded private AuditInfo auditInfo;

    public Dept() {
        // JPA Required
    }

    @Override
    public DeptId getId() {
        return id != null ? new DeptId(id) : null;
    }

    public void setId(DeptId id) {
        this.id = id != null ? id.value() : null;
    }

    @Override
    public DeptId getParentId() {
        return parentId != null ? new DeptId(parentId) : null;
    }

    @Override
    public void setParentId(DeptId parentId) {
        this.parentId = parentId != null ? parentId.value() : null;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public UserId getLeaderUserId() {
        return leaderUserId != null ? new UserId(leaderUserId) : null;
    }

    public void setLeaderUserId(UserId leaderUserId) {
        this.leaderUserId = leaderUserId != null ? leaderUserId.value() : null;
    }

    public DeptStatus getStatus() {
        return status;
    }

    public void setStatus(DeptStatus status) {
        this.status = status;
    }

    @Override
    public TenantId getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }
}
