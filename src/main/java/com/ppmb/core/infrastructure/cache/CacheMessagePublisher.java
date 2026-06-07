package com.ppmb.core.infrastructure.cache;

public interface CacheMessagePublisher {
    void publishInvalidation(String cacheName, Object key, boolean clearAll);
}
