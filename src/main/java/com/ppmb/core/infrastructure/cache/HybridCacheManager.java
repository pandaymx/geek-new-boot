package com.ppmb.core.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;

public class HybridCacheManager implements CacheManager {

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CacheMessagePublisher messagePublisher;
    private final Map<String, Class<?>> cacheTypes;

    public HybridCacheManager(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            CacheMessagePublisher messagePublisher,
            Map<String, Class<?>> cacheTypes) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.messagePublisher = messagePublisher;
        this.cacheTypes = cacheTypes != null ? cacheTypes : Collections.emptyMap();
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, this::createHybridCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    private Cache createHybridCache(String name) {
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache =
                Caffeine.newBuilder()
                        .maximumSize(10000)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .build();

        Class<?> valueType = cacheTypes.getOrDefault(name, Object.class);
        HybridCache cache =
                new HybridCache(name, caffeineCache, redisTemplate, objectMapper, valueType);

        return new CacheMessagePublishingWrapper(cache, messagePublisher);
    }

    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if (cache instanceof CacheMessagePublishingWrapper wrapper) {
            Cache target = wrapper.getTarget();
            if (target instanceof HybridCache hc) {
                hc.clearLocal(key);
            }
        } else if (cache instanceof HybridCache hc) {
            hc.clearLocal(key);
        }
    }
}
