package com.ppmb.core.infrastructure.cache;

import java.util.concurrent.Callable;
import org.springframework.cache.Cache;

class CacheMessagePublishingWrapper implements Cache {

    private final Cache target;
    private final CacheMessagePublisher messagePublisher;

    CacheMessagePublishingWrapper(Cache target, CacheMessagePublisher messagePublisher) {
        this.target = target;
        this.messagePublisher = messagePublisher;
    }

    public Cache getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public Object getNativeCache() {
        return target.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return target.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return target.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return target.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        target.put(key, value);
        messagePublisher.publishInvalidation(target.getName(), key, false);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper wrapper = target.putIfAbsent(key, value);
        if (wrapper == null) {
            messagePublisher.publishInvalidation(target.getName(), key, false);
        }
        return wrapper;
    }

    @Override
    public void evict(Object key) {
        target.evict(key);
        messagePublisher.publishInvalidation(target.getName(), key, false);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        boolean result = target.evictIfPresent(key);
        if (result) {
            messagePublisher.publishInvalidation(target.getName(), key, false);
        }
        return result;
    }

    @Override
    public void clear() {
        target.clear();
        messagePublisher.publishInvalidation(target.getName(), null, true);
    }

    @Override
    public boolean invalidate() {
        boolean result = target.invalidate();
        if (result) {
            messagePublisher.publishInvalidation(target.getName(), null, true);
        }
        return result;
    }
}
