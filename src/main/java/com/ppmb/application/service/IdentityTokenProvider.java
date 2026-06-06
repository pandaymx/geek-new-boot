package com.ppmb.application.service;

import java.util.Set;

/** 应用层身份令牌提供者接口（领域服务契约）。 业务层应仅依赖此接口，不关心底层是使用 JJWT 还是 Auth0 等具体技术实现，实现架构的完全解耦。 */
public interface IdentityTokenProvider {

    /**
     * 生成访问令牌（Access Token）。
     *
     * @param userId 用户唯一标识
     * @param username 用户名
     * @param roles 用户角色集合
     * @return 经过加密或签名的令牌字符串
     */
    String createAccessToken(Long userId, String username, Set<String> roles);

    /**
     * 校验令牌并获取解析后的载荷数据。
     *
     * @param token 原始令牌字符串
     * @return 校验通过后的载荷对象 {@link ClaimsPayload}
     * @throws InvalidTokenException 当令牌过期或无效时抛出该领域异常
     */
    ClaimsPayload verifyAndGetPayload(String token) throws InvalidTokenException;
}
