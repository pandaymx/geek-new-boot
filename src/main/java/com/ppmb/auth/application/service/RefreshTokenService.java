package com.ppmb.auth.application.service;

import com.ppmb.application.service.IdentityTokenProvider;
import com.ppmb.application.service.InvalidTokenException;
import com.ppmb.auth.presentation.dto.TokenPairResponse;
import com.ppmb.auth.spi.UserRoleProvider;
import com.ppmb.infra.security.JwtConfigProperties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** 刷新令牌服务（基于 Redis/Valkey）。 负责 Refresh Token 的签发、校验与续期（Rotation）。 */
@Service
public class RefreshTokenService {

    private static final String REFRESH_TOKEN_PREFIX = "rt:";

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtConfigProperties jwtConfigProperties;
    private final IdentityTokenProvider identityTokenProvider;
    private final UserRoleProvider userRoleProvider;

    public RefreshTokenService(
            StringRedisTemplate stringRedisTemplate,
            JwtConfigProperties jwtConfigProperties,
            IdentityTokenProvider identityTokenProvider,
            UserRoleProvider userRoleProvider) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtConfigProperties = jwtConfigProperties;
        this.identityTokenProvider = identityTokenProvider;
        this.userRoleProvider = userRoleProvider;
    }

    /**
     * 生成一个新的 Refresh Token，并将其存储在 Redis 中。
     *
     * @param userId 用户唯一标识
     * @param username 用户名
     * @return 返回生成的 UUID 字符串作为 Refresh Token
     */
    public String createRefreshToken(Long userId, String username) {
        String uuid = UUID.randomUUID().toString();
        String redisKey = getRedisKey(uuid);
        String redisValue = userId + ":" + username;

        long expirationDays = jwtConfigProperties.getRefreshTokenExpiration().toDays();

        stringRedisTemplate.opsForValue().set(redisKey, redisValue, expirationDays, TimeUnit.DAYS);

        return uuid;
    }

    /**
     * 执行滑窗续期（Token Rotation）。
     *
     * @param oldRefreshToken 旧的 Refresh Token
     * @return 包含新 AccessToken 和 新 RefreshToken 的结果对
     * @throws InvalidTokenException 当 Refresh Token 无效或过期时抛出
     */
    public TokenPairResponse rotateRefreshToken(String oldRefreshToken) {
        if (!StringUtils.hasText(oldRefreshToken)) {
            throw new InvalidTokenException("Refresh token is empty");
        }

        String redisKey = getRedisKey(oldRefreshToken);
        String redisValue = stringRedisTemplate.opsForValue().get(redisKey);

        if (!StringUtils.hasText(redisValue)) {
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }

        // 解析 Redis 中存储的值 {userId}:{username}
        String[] parts = redisValue.split(":", 2);
        if (parts.length != 2) {
            // 防御性处理：格式不正确，直接删掉该恶意或损坏的数据
            stringRedisTemplate.delete(redisKey);
            throw new InvalidTokenException("Refresh token data is corrupted");
        }

        Long userId;
        try {
            userId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            stringRedisTemplate.delete(redisKey);
            throw new InvalidTokenException("Refresh token data is corrupted", e);
        }
        String username = parts[1];

        // 成功验证后，无论如何先作废旧的 Refresh Token，防止重放攻击（Replay Attack）
        stringRedisTemplate.delete(redisKey);

        // 实时获取用户最新的权限角色，确保降权或改权生效
        Set<String> roles = userRoleProvider.getRolesByUserId(userId);

        // 重新签发 AccessToken
        String newAccessToken = identityTokenProvider.createAccessToken(userId, username, roles);

        // 签发新的 RefreshToken
        String newRefreshToken = createRefreshToken(userId, username);

        return new TokenPairResponse(newAccessToken, newRefreshToken);
    }

    private String getRedisKey(String tokenUuid) {
        return REFRESH_TOKEN_PREFIX + tokenUuid;
    }
}
