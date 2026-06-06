package com.ppmb.core.presentation.controller;

import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 系统配置端点，用于获取当前生效的区域信息等配置，辅助前端做状态同步。 */
@RestController
@RequestMapping("/api/v1/system/config")
public class SystemConfigController {

    @GetMapping
    public Map<String, Object> getSystemConfig() {
        return Map.of("currentLocale", LocaleContextHolder.getLocale().toString());
    }
}
