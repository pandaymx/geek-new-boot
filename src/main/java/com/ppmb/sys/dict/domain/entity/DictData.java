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
@Table(name = "sys_dict_data")
public class DictData extends AbstractEntity<Long> implements Auditable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "dict_type", length = 100, nullable = false)
    private String dictType;

    @Column(name = "dict_label", length = 100, nullable = false)
    private String dictLabel;

    @Column(name = "dict_value", length = 100, nullable = false)
    private String dictValue;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "list_class", length = 50)
    private String listClass;

    @Column(name = "status", nullable = false)
    private Integer status; // 0: Normal, 1: Disabled

    @Column(name = "remark", length = 500)
    private String remark;

    @Embedded private AuditInfo auditInfo = AuditInfo.empty();

    protected DictData() {}

    public DictData(
            Long id,
            String dictType,
            String dictLabel,
            String dictValue,
            Integer sortOrder,
            String listClass,
            Integer status,
            String remark) {
        this.id = id;
        this.dictType = dictType;
        this.dictLabel = dictLabel;
        this.dictValue = dictValue;
        this.sortOrder = sortOrder;
        this.listClass = listClass;
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

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getListClass() {
        return listClass;
    }

    public void setListClass(String listClass) {
        this.listClass = listClass;
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
