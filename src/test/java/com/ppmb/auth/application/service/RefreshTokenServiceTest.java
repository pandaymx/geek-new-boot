package com.ppmb.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ppmb.application.service.IdentityTokenProvider;
import com.ppmb.application.service.InvalidTokenException;
import com.ppmb.auth.presentation.dto.TokenPairResponse;
import com.ppmb.auth.spi.UserRoleProvider;
import com.ppmb.infra.security.JwtConfigProperties;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock private StringRedisTemplate stringRedisTemplate;

    @Mock private ValueOperations<String, String> valueOperations;

    @Mock private JwtConfigProperties jwtConfigProperties;

    @Mock private IdentityTokenProvider identityTokenProvider;

    @Mock private UserRoleProvider userRoleProvider;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService =
                new RefreshTokenService(
                        stringRedisTemplate,
                        jwtConfigProperties,
                        identityTokenProvider,
                        userRoleProvider);
    }

    @Test
    void createRefreshToken_ShouldStoreInRedisAndReturnUuid() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(jwtConfigProperties.getRefreshTokenExpiration()).thenReturn(Duration.ofDays(7));

        String uuid = refreshTokenService.createRefreshToken(1L, "testUser");

        assertThat(uuid).isNotBlank();

        // Assert valid UUID format
        assertThat(UUID.fromString(uuid)).isNotNull();

        verify(valueOperations).set("rt:" + uuid, "1:testUser", 7L, TimeUnit.DAYS);
    }

    @Test
    void rotateRefreshToken_ShouldDeleteOldAndGenerateNew_WhenValid() {
        String oldUuid = UUID.randomUUID().toString();
        String oldRedisKey = "rt:" + oldUuid;

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(oldRedisKey)).thenReturn("1:testUser");
        when(userRoleProvider.getRolesByUserId(1L)).thenReturn(Set.of("ROLE_USER"));
        when(identityTokenProvider.createAccessToken(
                        eq(1L), eq("testUser"), eq(Set.of("ROLE_USER"))))
                .thenReturn("new-access-token");
        when(jwtConfigProperties.getRefreshTokenExpiration()).thenReturn(Duration.ofDays(7));

        TokenPairResponse response = refreshTokenService.rotateRefreshToken(oldUuid);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotEqualTo(oldUuid);

        verify(stringRedisTemplate).delete(oldRedisKey);
        verify(valueOperations)
                .set("rt:" + response.refreshToken(), "1:testUser", 7L, TimeUnit.DAYS);
    }

    @Test
    void rotateRefreshToken_ShouldThrowException_WhenTokenEmpty() {
        assertThatThrownBy(() -> refreshTokenService.rotateRefreshToken(""))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Refresh token is empty");
    }

    @Test
    void rotateRefreshToken_ShouldThrowException_WhenTokenNotFoundInRedis() {
        String oldUuid = UUID.randomUUID().toString();
        String oldRedisKey = "rt:" + oldUuid;

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(oldRedisKey)).thenReturn(null);

        assertThatThrownBy(() -> refreshTokenService.rotateRefreshToken(oldUuid))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Refresh token is invalid or expired");
    }

    @Test
    void rotateRefreshToken_ShouldThrowExceptionAndCleanUp_WhenDataCorrupted1() {
        String oldUuid = UUID.randomUUID().toString();
        String oldRedisKey = "rt:" + oldUuid;

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(oldRedisKey)).thenReturn("invalid-format-no-colon");

        assertThatThrownBy(() -> refreshTokenService.rotateRefreshToken(oldUuid))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Refresh token data is corrupted");

        verify(stringRedisTemplate).delete(oldRedisKey);
    }

    @Test
    void rotateRefreshToken_ShouldThrowExceptionAndCleanUp_WhenDataCorrupted2() {
        String oldUuid = UUID.randomUUID().toString();
        String oldRedisKey = "rt:" + oldUuid;

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(oldRedisKey)).thenReturn("notAnId:username");

        assertThatThrownBy(() -> refreshTokenService.rotateRefreshToken(oldUuid))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Refresh token data is corrupted");

        verify(stringRedisTemplate).delete(oldRedisKey);
    }
}
