package com.ppmb.core.infrastructure.cache;

import java.io.Serializable;

/** 缓存失效消息，用于在集群中通过 Pub/Sub 广播 L1 缓存清理指令。 */
public record CacheInvalidationMessage(
        String cacheName, Object key, String nodeId, boolean clearAll) implements Serializable {}
