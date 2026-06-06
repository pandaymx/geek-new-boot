package com.ppmb.application.service;

import java.util.Set;

/**
 * 纯 Java Record，用于在应用层表示身份令牌（JWT）的载荷数据。 使用 Record 可以实现不可变数据传输对象，完美隔离第三方类库的依赖。
 *
 * @param userId 用户唯一标识
 * @param username 用户名（通常对应于标准的 JWT 'sub' 字段）
 * @param roles 用户的角色集合
 */
public record ClaimsPayload(Long userId, String username, Set<String> roles) {}
