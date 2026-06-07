package com.ppmb.auth.presentation.controller;

import com.ppmb.auth.application.service.RefreshTokenService;
import com.ppmb.auth.presentation.dto.RefreshTokenRequest;
import com.ppmb.auth.presentation.dto.TokenPairResponse;
import com.ppmb.core.presentation.response.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 认证控制器。 负责处理与用户认证、令牌刷新相关的请求。 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    public AuthController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * 刷新访问令牌。
     *
     * @param request 包含 Refresh Token 的请求体
     * @return 包含新 AccessToken 和新 RefreshToken 的响应
     */
    @PostMapping("/refresh")
    public Result<TokenPairResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPairResponse response = refreshTokenService.rotateRefreshToken(request.refreshToken());
        return Result.success(response);
    }
}
