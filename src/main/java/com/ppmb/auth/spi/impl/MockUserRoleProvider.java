package com.ppmb.auth.spi.impl;

import com.ppmb.auth.spi.UserRoleProvider;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 临时 Mock 实现的 {@link UserRoleProvider}。 仅在 "dev" 或 "local" 环境下生效，方便开发阶段调通 Token 刷新流程， 待 user
 * 模块补全真实实现后，可直接无缝切换。
 */
@Service
@Profile({"dev", "local"})
public class MockUserRoleProvider implements UserRoleProvider {
    @Override
    public Set<String> getRolesByUserId(Long userId) {
        // 返回模拟的角色集合，以便能够签发 Token
        return Set.of("ROLE_USER");
    }
}
