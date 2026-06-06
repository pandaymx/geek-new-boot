package com.ppmb.core.config.thread;

import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Global Thread Pool Configuration Strictly configures a virtual thread-based executor per the
 * project guidelines.
 */
@EnableAsync
@Configuration
public class ThreadPoolConfiguration {

    public static final String SYSTEM_LOG_EXECUTOR = "systemLogExecutor";

    /**
     * Virtual Thread executor for async logging and system tasks. Prevents blocking OS platform
     * threads.
     */
    @Bean(name = SYSTEM_LOG_EXECUTOR)
    public AsyncTaskExecutor systemLogExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
