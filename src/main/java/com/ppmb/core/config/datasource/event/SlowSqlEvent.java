package com.ppmb.core.config.datasource.event;

import org.springframework.context.ApplicationEvent;

/** Event triggered when a slow SQL query is detected. */
public class SlowSqlEvent extends ApplicationEvent {

    private final String traceId;
    private final String rawSql;
    private final String queryParams;
    private final long executionTime;

    public SlowSqlEvent(
            Object source, String traceId, String rawSql, String queryParams, long executionTime) {
        super(source);
        this.traceId = traceId;
        this.rawSql = rawSql;
        this.queryParams = queryParams;
        this.executionTime = executionTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getRawSql() {
        return rawSql;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
