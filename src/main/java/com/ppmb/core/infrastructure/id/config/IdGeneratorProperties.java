package com.ppmb.core.infrastructure.id.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ppmb.cluster")
public class IdGeneratorProperties {

    /** 分布式工作节点 ID (0-31) */
    private long workerId = 0L;

    /** 数据中心 ID (0-31) */
    private long dataCenterId = 0L;

    // --- Getters and Setters ---
    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
