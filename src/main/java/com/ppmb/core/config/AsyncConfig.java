package com.ppmb.core.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("cpuTaskExecutor")
    public Executor cpuTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int processors = Runtime.getRuntime().availableProcessors();

        // 核心线程数（Core Pool Size）：设置为当前机器的可用 CPU 核心数
        executor.setCorePoolSize(processors);

        // 最大线程数（Max Pool Size）：设置为可用 CPU 核心数 * 2
        executor.setMaxPoolSize(processors * 2);

        // 队列容量（Queue Capacity）：500
        executor.setQueueCapacity(500);

        // 线程名称前缀：设置为 "cpu-heavy-pool-"
        executor.setThreadNamePrefix("cpu-heavy-pool-");

        // 拒绝策略：使用 CallerRunsPolicy（让调用者线程执行，作为兜底缓冲）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 非核心线程的存活时间：60秒
        executor.setKeepAliveSeconds(60);

        // 优雅停机：等待任务执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 优雅停机：最长等待时间 30秒
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        return executor;
    }

    @Bean("virtualTaskExecutor")
    public Executor virtualTaskExecutor() {
        // 直接使用 Java 21+ 原生的 Executors.newVirtualThreadPerTaskExecutor() 并用 TaskExecutorAdapter 包装
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}
