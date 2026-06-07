package com.ppmb.infra.security;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** JWT 配置属性类。 绑定 `jwt.*` 配置前缀，提供密钥与过期时间的设置。 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigProperties {

    /** 密钥：默认值为一个安全的占位符，但在生产环境中应该被覆盖 */
    private String secret = "default-secret-key-that-is-at-least-256-bits-long-for-hmac";

    /** Access Token 过期时间，默认为 30 分钟 */
    private Duration accessTokenExpiration = Duration.ofMinutes(30);

    /** Refresh Token 过期时间，默认为 7 天 */
    private Duration refreshTokenExpiration = Duration.ofDays(7);

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(Duration accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public Duration getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Duration refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
}
