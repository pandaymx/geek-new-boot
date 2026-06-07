package com.ppmb.auth.spi;

import java.util.Set;

/**
 * 用户角色提供者接口（SPI）。 用于在 auth 模块中获取用户的最新角色集合，实际实现由外部模块（如 user 模块）提供， 从而在 Token
 * 刷新等场景中，保证权限流转的时效性与架构的解耦。
 */
public interface UserRoleProvider {
    /**
     * 根据用户唯一标识获取其最新的角色集合。
     *
     * @param userId 用户的唯一标识 ID
     * @return 最新的角色集合，例如 "ROLE_USER", "ROLE_ADMIN"
     */
    Set<String> getRolesByUserId(Long userId);
}
