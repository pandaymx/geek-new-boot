package com.ppmb.sys.application.listener;

import com.ppmb.core.config.datasource.event.SlowSqlEvent;
import com.ppmb.core.config.thread.ThreadPoolConfiguration;
import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.sys.domain.model.SysSlowSqlLog;
import com.ppmb.sys.infrastructure.repository.SysSlowSqlLogRepository;
import java.time.LocalDateTime;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** Listens for SlowSqlEvent and persists the log asynchronously. */
@Component
public class SlowSqlEventListener {

    private final SysSlowSqlLogRepository repository;
    private final SnowflakeIdGenerator idGenerator;

    public SlowSqlEventListener(
            SysSlowSqlLogRepository repository, SnowflakeIdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    /** Asynchronously handles slow SQL events using the designated virtual thread pool. */
    @Async(ThreadPoolConfiguration.SYSTEM_LOG_EXECUTOR)
    @EventListener
    public void onSlowSqlEvent(SlowSqlEvent event) {
        SysSlowSqlLog log =
                new SysSlowSqlLog(
                        idGenerator.nextId(),
                        event.getTraceId(),
                        event.getRawSql(),
                        event.getQueryParams(),
                        event.getExecutionTime(),
                        LocalDateTime.now());
        repository.save(log);
    }
}
