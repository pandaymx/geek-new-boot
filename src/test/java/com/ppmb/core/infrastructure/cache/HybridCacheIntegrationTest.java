package com.ppmb.core.infrastructure.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppmb.core.config.CacheConfiguration;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
        classes = {com.ppmb.GeekNewApplication.class, HybridCacheIntegrationTest.TestConfig.class},
        properties = {
            "spring.modulith.republish-outstanding-events-on-restart=false",
            "spring.main.web-application-type=none",
            "spring.main.allow-bean-definition-overriding=true"
        })
@Testcontainers
class HybridCacheIntegrationTest {

    @Container
    @org.springframework.boot.testcontainers.service.connection.ServiceConnection(name = "redis")
    static GenericContainer<?> valkeyContainer =
            new GenericContainer<>("valkey/valkey:latest").withExposedPorts(6379);

    @Configuration
    @Import({CacheConfiguration.class})
    static class TestConfig {
        @Bean
        public TestCacheService testCacheService() {
            return new TestCacheService();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Service
    @RegisterReflectionForBinding({CacheInvalidationMessage.class, String.class})
    public static class TestCacheService {
        private final AtomicInteger invocationCount = new AtomicInteger(0);

        @Cacheable(cacheNames = "testCache", key = "#id")
        public String getValue(String id) {
            invocationCount.incrementAndGet();
            return "Value-" + id;
        }

        @CachePut(cacheNames = "testCache", key = "#id")
        public String updateValue(String id, String newValue) {
            return newValue;
        }

        @CacheEvict(cacheNames = "testCache", key = "#id")
        public void evictValue(String id) {}

        @CacheEvict(cacheNames = "testCache", allEntries = true)
        public void evictAll() {}

        public int getInvocationCount() {
            return invocationCount.get();
        }

        public void resetInvocationCount() {
            invocationCount.set(0);
        }
    }

    @Autowired private TestCacheService cacheService;

    @Autowired private HybridCacheManager cacheManager;

    @Autowired private StringRedisTemplate stringRedisTemplate;

    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        cacheService.resetInvocationCount();
        stringRedisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        cacheManager.clearLocal("testCache", null);
    }

    @Test
    void testL1AndL2CacheFlow() {
        // 1st read: Miss -> DB (invocation count = 1), writes to L1 & L2
        String value1 = cacheService.getValue("1");
        assertThat(value1).isEqualTo("Value-1");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);

        // 2nd read: Hit L1 -> No DB invocation
        String value2 = cacheService.getValue("1");
        assertThat(value2).isEqualTo("Value-1");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);

        // Clear local L1 cache manually
        cacheManager.clearLocal("testCache", "1");

        // 3rd read: Miss L1 -> Hit L2 -> Repopulates L1, No DB invocation
        String value3 = cacheService.getValue("1");
        assertThat(value3).isEqualTo("Value-1");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);
    }

    @Test
    void testPubSubInvalidationStorm() throws Exception {
        cacheService.getValue("2");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);

        assertThat(cacheManager.getCache("testCache").get("2").get()).isEqualTo("Value-2");

        CacheInvalidationMessage msg =
                new CacheInvalidationMessage("testCache", "2", "other-node-id", false);
        stringRedisTemplate.convertAndSend(
                "cache:invalidation:topic", objectMapper.writeValueAsString(msg));

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(
                        () -> {
                            stringRedisTemplate.delete("testCache:2");
                            cacheService.getValue("2");
                            assertThat(cacheService.getInvocationCount()).isEqualTo(2);
                        });
    }

    @Test
    void testSpringCacheAnnotations() {
        cacheService.getValue("3");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);

        cacheService.updateValue("3", "NewValue");

        assertThat(cacheService.getValue("3")).isEqualTo("NewValue");
        assertThat(cacheService.getInvocationCount()).isEqualTo(1);

        String l2Value = stringRedisTemplate.opsForValue().get("testCache:3");
        assertThat(l2Value).contains("NewValue");

        cacheService.evictValue("3");

        assertThat(stringRedisTemplate.hasKey("testCache:3")).isFalse();

        cacheService.getValue("3");
        assertThat(cacheService.getInvocationCount()).isEqualTo(2);

        cacheService.getValue("4");
        cacheService.getValue("5");
        assertThat(cacheService.getInvocationCount()).isEqualTo(4);

        cacheService.evictAll();

        Boolean hasKey4 = stringRedisTemplate.hasKey("testCache:4");
        Boolean hasKey5 = stringRedisTemplate.hasKey("testCache:5");
        assertThat(hasKey4 != null && hasKey4).isFalse();
        assertThat(hasKey5 != null && hasKey5).isFalse();

        cacheService.getValue("4");
        assertThat(cacheService.getInvocationCount()).isEqualTo(5);
    }
}
