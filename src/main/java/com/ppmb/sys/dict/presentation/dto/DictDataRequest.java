package com.ppmb.sys.dict.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DictDataRequest(
        @NotBlank(message = "{validation.dictType.notBlank}") String dictType,
        @NotBlank(message = "{validation.dictLabel.notBlank}") String dictLabel,
        @NotBlank(message = "{validation.dictValue.notBlank}") String dictValue,
        @NotNull(message = "{validation.sortOrder.notNull}") Integer sortOrder,
        String listClass,
        @NotNull(message = "{validation.status.notNull}") Integer status,
        String remark) {}
