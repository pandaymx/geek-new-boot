package com.ppmb.sys.config;

import java.time.Instant;

public record SysConfigDTO(
        Long id,
        String configName,
        String configKey,
        String configValue,
        ConfigType configType,
        String remark,
        String createdBy,
        Instant createdTime,
        String updatedBy,
        Instant updatedTime) {}
