package com.ppmb.core.infrastructure.id;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("雪花算法 ID 生成器测试")
class SnowflakeIdGeneratorTest {

    @Test
    @DisplayName("基础功能：生成的 ID 应该单调递增且不为空")
    void testBasicIdGeneration() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        long id1 = generator.nextId();
        long id2 = generator.nextId();

        assertNotNull(id1);
        assertNotNull(id2);
        // 验证时间戳和序列号递增导致后生成的 ID 大于先生成的 ID
        assertTrue(id2 > id1, "后生成的ID应该大于先生成的ID");
    }

    @Test
    @DisplayName("高并发测试：多线程并发生成 10 万个 ID，验证绝对唯一性")
    void testConcurrentIdGenerationUnique() throws InterruptedException {
        final int threadCount = 100; // 100 个并发线程
        final int idPerThread = 1000; // 每个线程生成 1000 个 ID，共计 10 万个
        final int totalIds = threadCount * idPerThread;

        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        // 线程安全的 Set，用来收集所有生成的 ID 并做去重校验
        Set<Long> idSet = Collections.synchronizedSet(new HashSet<>(totalIds));

        // 使用 Java 现代化的线程池和发令枪（CountDownLatch）控制并发
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch endLatch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(
                        () -> {
                            try {
                                startLatch.await(); // 所有线程在此等待，听候发令枪同时开跑
                                for (int j = 0; j < idPerThread; j++) {
                                    idSet.add(generator.nextId());
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                endLatch.countDown();
                            }
                        });
            }

            long startTime = System.currentTimeMillis();
            startLatch.countDown(); // 发令枪响！100个线程同时疯狂生成 ID
            endLatch.await(); // 等待所有线程执行完毕
            long duration = System.currentTimeMillis() - startTime;

            System.out.printf("成功生成 %d 个 ID，耗时: %d 毫秒\n", totalIds, duration);

            // 核心断言：如果 Set 内部的实际大小等于总生成量，说明 10 万个 ID 里没有任何一个重复
            assertEquals(totalIds, idSet.size(), "并发环境下生成的 ID 存在重复！");
        }
    }
}
