package com.ppmb.core.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class CacheInvalidationMessageListener implements MessageListener {

    private static final Logger log =
            LoggerFactory.getLogger(CacheInvalidationMessageListener.class);

    private final HybridCacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private final String currentNodeId;

    public CacheInvalidationMessageListener(
            HybridCacheManager cacheManager, ObjectMapper objectMapper, String currentNodeId) {
        this.cacheManager = cacheManager;
        this.objectMapper = objectMapper;
        this.currentNodeId = currentNodeId;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            CacheInvalidationMessage invalidationMessage =
                    objectMapper.readValue(message.getBody(), CacheInvalidationMessage.class);

            if (currentNodeId.equals(invalidationMessage.nodeId())) {
                log.trace(
                        "Received own cache invalidation message, ignored. cacheName={}",
                        invalidationMessage.cacheName());
                return;
            }

            log.debug(
                    "Received cache invalidation message from node {}: {}",
                    invalidationMessage.nodeId(),
                    invalidationMessage);

            if (invalidationMessage.clearAll()) {
                cacheManager.clearLocal(invalidationMessage.cacheName(), null);
            } else {
                cacheManager.clearLocal(invalidationMessage.cacheName(), invalidationMessage.key());
            }
        } catch (Exception e) {
            log.error("Failed to process cache invalidation message", e);
        }
    }
}
