package com.ppmb.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ppmb.application.service.ClaimsPayload;
import com.ppmb.application.service.IdentityTokenProvider;
import com.ppmb.application.service.InvalidTokenException;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

/** JWT 令牌提供者的具体实现（基础设施层）。 实现了基于 com.auth0:java-jwt 库的身份令牌签发与校验机制，通过 Spring IoC 容器注册。 */
@Component
public class JwtTokenProviderImpl implements IdentityTokenProvider {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";

    private final JwtConfigProperties jwtConfigProperties;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public JwtTokenProviderImpl(JwtConfigProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

    /** 在 bean 初始化后，根据配置的 secret 安全地初始化加密算法对象和验证器。 */
    @PostConstruct
    public void init() {
        // 使用 HMAC256 算法，并传入从配置中读取的密钥
        this.algorithm = Algorithm.HMAC256(jwtConfigProperties.getSecret());
        // 构建重用的验证器，这里未显式要求验证 issuer，但验证签名和默认的过期时间
        this.verifier = JWT.require(algorithm).build();
    }

    @Override
    public String createAccessToken(Long userId, String username, Set<String> roles) {
        Instant now = Instant.now();
        Instant expiryDate =
                now.plusMillis(jwtConfigProperties.getAccessTokenExpiration().toMillis());

        // Java 21+ 支持流式处理等优雅操作。此处直接利用集合转换
        List<String> rolesList = (roles != null) ? List.copyOf(roles) : List.of();

        return JWT.create()
                // 标准的 sub (Subject) 字段用来存放传入的 username
                .withSubject(username)
                // 签发时间 (iat)
                .withIssuedAt(Date.from(now))
                // 过期时间 (exp)
                .withExpiresAt(Date.from(expiryDate))
                // 自定义载荷：userId 和 roles
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_ROLES, rolesList)
                .sign(algorithm);
    }

    @Override
    public ClaimsPayload verifyAndGetPayload(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);

            // 解析标准的 sub 为 username
            String username = decodedJWT.getSubject();

            // 解析自定义的 claims
            Long userId = decodedJWT.getClaim(CLAIM_USER_ID).asLong();
            List<String> rolesList = decodedJWT.getClaim(CLAIM_ROLES).asList(String.class);
            Set<String> roles = (rolesList != null) ? new HashSet<>(rolesList) : Set.of();

            return new ClaimsPayload(userId, username, roles);
        } catch (JWTVerificationException exception) {
            // 捕获并处理 Auth0 的签名错误、过期等异常，统一抛出领域异常
            throw new InvalidTokenException("Invalid or expired identity token", exception);
        }
    }
}
