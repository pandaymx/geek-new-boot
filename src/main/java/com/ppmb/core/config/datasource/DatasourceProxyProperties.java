package com.ppmb.core.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ppmb.datasource")
public class DatasourceProxyProperties {

    /** The threshold in milliseconds for a SQL query to be considered "slow". Default is 500ms. */
    private long slowSqlThresholdMs = 500L;

    public long getSlowSqlThresholdMs() {
        return slowSqlThresholdMs;
    }

    public void setSlowSqlThresholdMs(long slowSqlThresholdMs) {
        this.slowSqlThresholdMs = slowSqlThresholdMs;
    }
}
