package com.ppmb.core.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

class HybridCache implements Cache {

    private static final Logger log = LoggerFactory.getLogger(HybridCache.class);

    private final String name;
    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String cachePrefix;
    private final Class<?> valueType;

    HybridCache(
            String name,
            com.github.benmanes.caffeine.cache.Cache<Object, Object> localCache,
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            Class<?> valueType) {
        this.name = name;
        this.localCache = localCache;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachePrefix = name + ":";
        this.valueType = valueType != null ? valueType : Object.class;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (key == null) return null;

        Object localValue = localCache.getIfPresent(key);
        if (localValue != null) {
            log.trace("L1 cache hit: cache={}, key={}", name, key);
            return new SimpleValueWrapper(localValue);
        }

        String redisKey = buildRedisKey(key);
        String redisValue = redisTemplate.opsForValue().get(redisKey);

        if (StringUtils.hasText(redisValue)) {
            try {
                Object value = objectMapper.readValue(redisValue, valueType);
                log.trace("L2 cache hit: cache={}, key={}", name, key);
                localCache.put(key, value);
                return new SimpleValueWrapper(value);
            } catch (Exception e) {
                log.warn(
                        "Failed to deserialize value from Redis for cache={}, key={}",
                        name,
                        key,
                        e);
            }
        }

        log.trace("Cache miss: cache={}, key={}", name, key);
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper == null) return null;
        Object value = wrapper.get();
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        @SuppressWarnings("unchecked")
        T result = (T) value;
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }

        synchronized (this) {
            wrapper = get(key);
            if (wrapper != null) {
                return (T) wrapper.get();
            }

            T value;
            try {
                value = valueLoader.call();
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
            put(key, value);
            return value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (key == null || value == null) return;

        String redisKey = buildRedisKey(key);
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(redisKey, jsonValue, java.time.Duration.ofHours(2));
        } catch (Exception e) {
            log.error("Failed to serialize value for Redis for cache={}, key={}", name, key, e);
            return;
        }

        localCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper existingValue = get(key);
        if (existingValue == null) {
            put(key, value);
        }
        return existingValue;
    }

    @Override
    public void evict(Object key) {
        if (key == null) return;
        redisTemplate.delete(buildRedisKey(key));
        localCache.invalidate(key);
    }

    @Override
    public void clear() {
        try {
            ScanOptions options =
                    ScanOptions.scanOptions().match(cachePrefix + "*").count(100).build();
            redisTemplate.execute(
                    (org.springframework.data.redis.connection.RedisConnection connection) -> {
                        try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                            while (cursor.hasNext()) {
                                redisTemplate.delete(
                                        new String(
                                                cursor.next(),
                                                java.nio.charset.StandardCharsets.UTF_8));
                            }
                        }
                        return null;
                    });
        } catch (Exception e) {
            log.error("Failed to clear L2 cache for cache={}", name, e);
        }
        localCache.invalidateAll();
    }

    public void clearLocal(Object key) {
        if (key != null) {
            localCache.invalidate(key);
        } else {
            localCache.invalidateAll();
        }
    }

    private String buildRedisKey(Object key) {
        return cachePrefix + key.toString();
    }
}
