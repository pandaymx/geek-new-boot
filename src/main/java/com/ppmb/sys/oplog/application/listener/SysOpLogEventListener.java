package com.ppmb.sys.oplog.application.listener;

import com.ppmb.core.config.thread.ThreadPoolConfiguration;
import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.core.log.domain.event.OpLogEvent;
import com.ppmb.sys.oplog.domain.entity.SysOpLog;
import com.ppmb.sys.oplog.infrastructure.repository.SysOpLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** Listens for OpLogEvent and persists the log asynchronously to the database. */
@Component
public class SysOpLogEventListener {

    private final SysOpLogRepository repository;
    private final SnowflakeIdGenerator idGenerator;

    public SysOpLogEventListener(SysOpLogRepository repository, SnowflakeIdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    /** Asynchronously handles operation log events using the designated virtual thread pool. */
    @Async(ThreadPoolConfiguration.SYSTEM_LOG_EXECUTOR)
    @EventListener
    public void onOpLogEvent(OpLogEvent event) {
        SysOpLog log =
                new SysOpLog(
                        idGenerator.nextId(),
                        event.traceId(),
                        event.tenantId(),
                        event.operatorId(),
                        event.operatorName(),
                        event.title(),
                        event.businessType(),
                        event.requestUrl(),
                        event.requestMethod(),
                        event.requestParam(),
                        event.status(),
                        event.errorMsg(),
                        event.operateTime());
        repository.save(log);
    }
}
