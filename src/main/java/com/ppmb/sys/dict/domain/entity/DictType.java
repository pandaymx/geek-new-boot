package com.ppmb.sys.dict.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.model.AuditInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_dict_type")
public class DictType extends AbstractEntity<Long> implements Auditable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "dict_name", length = 100, nullable = false)
    private String dictName;

    @Column(name = "dict_type", length = 100, nullable = false, unique = true)
    private String dictType;

    @Column(name = "status", nullable = false)
    private Integer status; // 0: Normal, 1: Disabled

    @Column(name = "remark", length = 500)
    private String remark;

    @Embedded private AuditInfo auditInfo = AuditInfo.empty();

    protected DictType() {}

    public DictType(Long id, String dictName, String dictType, Integer status, String remark) {
        this.id = id;
        this.dictName = dictName;
        this.dictType = dictType;
        this.status = status;
        this.remark = remark;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
