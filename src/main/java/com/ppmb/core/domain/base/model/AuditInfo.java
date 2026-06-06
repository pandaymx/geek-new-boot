package com.ppmb.core.domain.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Embeddable
public class AuditInfo {

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Instant createdTime;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_time")
    private Instant updatedTime;

    // TODO: Add createdByDept field
    // @Column(name = "created_by_dept")
    // private String createdByDept;

    protected AuditInfo() {
        // Required by JPA
    }

    public AuditInfo(String createdBy, Instant createdTime, String updatedBy, Instant updatedTime) {
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
    }

    public static AuditInfo empty() {
        return new AuditInfo(null, null, null, null);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditInfo auditInfo = (AuditInfo) o;
        return Objects.equals(createdBy, auditInfo.createdBy)
                && Objects.equals(createdTime, auditInfo.createdTime)
                && Objects.equals(updatedBy, auditInfo.updatedBy)
                && Objects.equals(updatedTime, auditInfo.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, createdTime, updatedBy, updatedTime);
    }
}
