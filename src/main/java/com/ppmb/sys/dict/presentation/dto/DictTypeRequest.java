package com.ppmb.sys.dict.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DictTypeRequest(
        @NotBlank(message = "{validation.dictName.notBlank}") String dictName,
        @NotBlank(message = "{validation.dictType.notBlank}") String dictType,
        @NotNull(message = "{validation.status.notNull}") Integer status,
        String remark) {}
