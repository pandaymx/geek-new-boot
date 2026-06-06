package com.ppmb.infra.security;

import static org.junit.jupiter.api.Assertions.*;

import com.ppmb.application.service.ClaimsPayload;
import com.ppmb.application.service.InvalidTokenException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtTokenProviderImplTest {

    private JwtTokenProviderImpl jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtConfigProperties properties = new JwtConfigProperties();
        properties.setSecret("my-super-secret-key-for-testing-only");
        properties.setExpiration(10000L); // 10 seconds expiration for testing
        jwtTokenProvider = new JwtTokenProviderImpl(properties);
        jwtTokenProvider.init();
    }

    @Test
    void testCreateAndVerifyTokenSuccess() {
        Long userId = 1001L;
        String username = "johndoe";
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");

        String token = jwtTokenProvider.createAccessToken(userId, username, roles);
        assertNotNull(token);

        ClaimsPayload payload = jwtTokenProvider.verifyAndGetPayload(token);
        assertNotNull(payload);
        assertEquals(userId, payload.userId());
        assertEquals(username, payload.username());
        assertTrue(payload.roles().containsAll(roles));
    }

    @Test
    void testVerifyTokenThrowsExceptionOnInvalidToken() {
        String invalidToken = "header.payload.invalidsignature";

        assertThrows(
                InvalidTokenException.class,
                () -> {
                    jwtTokenProvider.verifyAndGetPayload(invalidToken);
                });
    }

    @Test
    void testExpiredTokenThrowsException() throws InterruptedException {
        JwtConfigProperties properties = new JwtConfigProperties();
        properties.setSecret("my-super-secret-key-for-testing-only");
        properties.setExpiration(1L); // 1 millisecond
        JwtTokenProviderImpl shortLivedProvider = new JwtTokenProviderImpl(properties);
        shortLivedProvider.init();

        String token = shortLivedProvider.createAccessToken(1L, "user", Set.of("USER"));

        // Wait to ensure token is expired
        Thread.sleep(10);

        assertThrows(
                InvalidTokenException.class,
                () -> {
                    shortLivedProvider.verifyAndGetPayload(token);
                });
    }
}
