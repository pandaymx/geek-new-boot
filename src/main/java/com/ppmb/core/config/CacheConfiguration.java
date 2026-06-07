package com.ppmb.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppmb.core.infrastructure.cache.CacheInvalidationMessage;
import com.ppmb.core.infrastructure.cache.CacheInvalidationMessageListener;
import com.ppmb.core.infrastructure.cache.CacheMessagePublisher;
import com.ppmb.core.infrastructure.cache.HybridCacheManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private static final String CACHE_INVALIDATION_TOPIC = "cache:invalidation:topic";

    private final String nodeId = UUID.randomUUID().toString();

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public CacheMessagePublisher cacheMessagePublisher(
            StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        return (cacheName, key, clearAll) -> {
            try {
                CacheInvalidationMessage message =
                        new CacheInvalidationMessage(cacheName, key, nodeId, clearAll);
                String jsonMessage = objectMapper.writeValueAsString(message);
                stringRedisTemplate.convertAndSend(CACHE_INVALIDATION_TOPIC, jsonMessage);
            } catch (Exception e) {
                throw new RuntimeException("Failed to publish cache invalidation message", e);
            }
        };
    }

    @Bean
    public CacheManager cacheManager(
            StringRedisTemplate stringRedisTemplate,
            ObjectMapper objectMapper,
            CacheMessagePublisher cacheMessagePublisher) {

        // 显式指定各个缓存的目标类型
        Map<String, Class<?>> cacheTypes = new HashMap<>();
        cacheTypes.put("testCache", String.class);
        // 如果有自定义 DTO: cacheTypes.put("userCache", UserDTO.class);

        return new HybridCacheManager(
                stringRedisTemplate, objectMapper, cacheMessagePublisher, cacheTypes);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            CacheInvalidationMessageListener cacheInvalidationMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(
                new MessageListenerAdapter(cacheInvalidationMessageListener, "onMessage"),
                new ChannelTopic(CACHE_INVALIDATION_TOPIC));
        return container;
    }

    @Bean
    public CacheInvalidationMessageListener cacheInvalidationMessageListener(
            CacheManager cacheManager, ObjectMapper objectMapper) {
        return new CacheInvalidationMessageListener(
                (HybridCacheManager) cacheManager, objectMapper, nodeId);
    }
}
