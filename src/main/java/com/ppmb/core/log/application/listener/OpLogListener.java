package com.ppmb.core.log.application.listener;

import com.ppmb.core.log.domain.event.OpLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/** 审计日志监听器，将拦截到的操作以结构化的形式写入特定的 Log4j2 Appender。 */
@Component
public class OpLogListener {

    private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("AUDIT_LOGGER");
    private static final Logger log = LoggerFactory.getLogger(OpLogListener.class);

    private final ObjectMapper objectMapper;

    public OpLogListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** 异步监听审计日志事件 使用 Spring Modulith 的 ApplicationModuleListener 进行基于虚拟线程/异步框架的事件消费 */
    @ApplicationModuleListener
    public void onOpLogEvent(OpLogEvent event) {
        try {
            // 将事件序列化为 JSON 以便结构化记录
            String jsonLog = objectMapper.writeValueAsString(event);
            AUDIT_LOGGER.info("OpLog - {}", jsonLog);
        } catch (Exception e) {
            log.error("Failed to serialize and log OpLogEvent: {}", event, e);
        }
    }
}
