package com.ppmb.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** 刷新 Token 的请求报文。 */
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token cannot be blank")
                @Pattern(
                        regexp =
                                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
                        message = "Invalid refresh token format")
                String refreshToken) {}
