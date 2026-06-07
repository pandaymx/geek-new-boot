package com.ppmb.sys.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SysConfigSaveRequest(
        @NotBlank @Size(max = 100) String configName,
        @NotBlank @Size(max = 100) String configKey,
        @NotBlank @Size(max = 500) String configValue,
        @NotNull ConfigType configType,
        @Size(max = 500) String remark) {}
