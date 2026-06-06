package com.ppmb.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** JWT 配置属性类。 绑定 `jwt.*` 配置前缀，提供密钥与过期时间的设置。 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

    /** 密钥：默认值为一个安全的占位符，但在生产环境中应该被覆盖 */
    private String secret = "default-secret-key-that-is-at-least-256-bits-long-for-hmac";

    /** 过期时间（毫秒），默认为 2 小时（2 * 60 * 60 * 1000 = 7200000 毫秒） */
    private long expiration = 7200000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
