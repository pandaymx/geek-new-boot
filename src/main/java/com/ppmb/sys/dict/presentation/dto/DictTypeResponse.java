package com.ppmb.sys.dict.presentation.dto;

import com.ppmb.sys.dict.domain.entity.DictType;

public record DictTypeResponse(
        Long id, String dictName, String dictType, Integer status, String remark) {
    public static DictTypeResponse from(DictType entity) {
        return new DictTypeResponse(
                entity.getId(),
                entity.getDictName(),
                entity.getDictType(),
                entity.getStatus(),
                entity.getRemark());
    }
}
