package com.ppmb.user.domain.model;

public enum IdentityType {
    PASSWORD("PASSWORD", "密码登录"),
    PHONE("PHONE", "手机验证码"),
    WECHAT("WECHAT", "微信三方"),
    OAUTH2("OAUTH2", "OAuth2登录");

    private final String code;
    private final String description;

    IdentityType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
