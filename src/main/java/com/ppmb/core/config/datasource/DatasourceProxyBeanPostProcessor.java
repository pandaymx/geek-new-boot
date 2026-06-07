package com.ppmb.core.config.datasource;

import com.ppmb.core.config.datasource.event.SlowSqlEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Wraps any initialized DataSource with datasource-proxy to intercept queries. Checks for slow SQL
 * based on ppmb.datasource.slow-sql-threshold-ms and publishes an event.
 */
@Component
public class DatasourceProxyBeanPostProcessor implements BeanPostProcessor {

    private final DatasourceProxyProperties properties;
    private final ApplicationEventPublisher eventPublisher;

    public DatasourceProxyBeanPostProcessor(
            DatasourceProxyProperties properties, ApplicationEventPublisher eventPublisher) {
        this.properties = properties;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof DataSource) {
            DataSource dataSource = (DataSource) bean;
            return ProxyDataSourceBuilder.create(dataSource)
                    .name(beanName + "-proxy")
                    .listener(new SlowSqlDetectionListener())
                    .build();
        }
        return bean;
    }

    private class SlowSqlDetectionListener implements QueryExecutionListener {
        @Override
        public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
            // Do nothing before query
        }

        @Override
        public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {
            long threshold = properties.getSlowSqlThresholdMs();
            long executionTime = execInfo.getElapsedTime();

            if (executionTime > threshold) {
                // Fetch traceId from MDC
                String traceId = MDC.get("traceId"); // Assuming 'traceId' is the MDC key used

                for (QueryInfo queryInfo : queryInfoList) {
                    String rawSql = queryInfo.getQuery();
                    String queryParams = formatParameters(queryInfo.getParametersList());

                    // Publish the event to be processed asynchronously
                    eventPublisher.publishEvent(
                            new SlowSqlEvent(this, traceId, rawSql, queryParams, executionTime));
                }
            }
        }

        private String formatParameters(List<List<ParameterSetOperation>> parametersList) {
            if (parametersList == null || parametersList.isEmpty()) {
                return "[]";
            }
            // Format parameters for logging, taking the first list for simplicity in single queries
            // In batch, this could be multiple lists
            return parametersList.stream()
                    .map(
                            params -> {
                                Map<String, String> paramMap =
                                        params.stream()
                                                .collect(
                                                        Collectors.toMap(
                                                                p -> p.getArgs()[0].toString(),
                                                                p -> {
                                                                    Object val = p.getArgs()[1];
                                                                    return val != null
                                                                            ? val.toString()
                                                                            : "null";
                                                                },
                                                                (existing, replacement) ->
                                                                        existing));
                                return paramMap.toString();
                            })
                    .collect(Collectors.joining(", ", "[", "]"));
        }
    }
}
