package com.ppmb.sys.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/** Entity representing a slow SQL execution log. */
@Entity
@Table(name = "sys_slow_sql_log")
public class SysSlowSqlLog {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "raw_sql", columnDefinition = "TEXT")
    private String rawSql;

    @Column(name = "query_params")
    private String queryParams;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // Constructors, Getters, and Setters

    protected SysSlowSqlLog() {
        // Default constructor for JPA
    }

    public SysSlowSqlLog(
            Long id,
            String traceId,
            String rawSql,
            String queryParams,
            Long executionTime,
            LocalDateTime createTime) {
        this.id = id;
        this.traceId = traceId;
        this.rawSql = rawSql;
        this.queryParams = queryParams;
        this.executionTime = executionTime;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRawSql() {
        return rawSql;
    }

    public void setRawSql(String rawSql) {
        this.rawSql = rawSql;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
