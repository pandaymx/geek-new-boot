package com.ppmb.sys.oplog.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.sys.oplog.domain.model.SysOpLogId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/** Audit log entity representing system operations. */
@Entity
@Table(name = "sys_op_log")
public class SysOpLog extends AbstractEntity<SysOpLogId> {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "trace_id", length = 64)
    private String traceId;

    @Column(name = "tenant_id", length = 64)
    private String tenantId;

    @Column(name = "operator_id", length = 64)
    private String operatorId;

    @Column(name = "operator_name", length = 100)
    private String operatorName;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "business_type", length = 50)
    private String businessType;

    @Column(name = "request_url", length = 500)
    private String requestUrl;

    @Column(name = "request_method", length = 20)
    private String requestMethod;

    @Column(name = "request_param", columnDefinition = "TEXT")
    private String requestParam;

    @Column(name = "status")
    private Integer status;

    @Column(name = "error_msg", columnDefinition = "TEXT")
    private String errorMsg;

    @Column(name = "operate_time", nullable = false, updatable = false)
    private Instant operateTime;

    protected SysOpLog() {
        // Default constructor for JPA
    }

    public SysOpLog(
            Long id,
            String traceId,
            String tenantId,
            String operatorId,
            String operatorName,
            String title,
            String businessType,
            String requestUrl,
            String requestMethod,
            String requestParam,
            Integer status,
            String errorMsg,
            Instant operateTime) {
        this.id = id;
        this.traceId = traceId;
        this.tenantId = tenantId;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.title = title;
        this.businessType = businessType;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.requestParam = requestParam;
        this.status = status;
        this.errorMsg = errorMsg;
        this.operateTime = operateTime;
    }

    @Override
    public SysOpLogId getId() {
        return id != null ? new SysOpLogId(id) : null;
    }

    public void setId(SysOpLogId id) {
        this.id = id != null ? id.value() : null;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Instant getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Instant operateTime) {
        this.operateTime = operateTime;
    }
}
