package com.ppmb.core.log.domain.event;

import java.time.Instant;

/** 审计日志领域事件 */
public record OpLogEvent(
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
        Instant operateTime) {}
