package com.ppmb.sys.dict.presentation.dto;

import com.ppmb.sys.dict.domain.entity.DictData;

public record DictDataResponse(
        Long id,
        String dictType,
        String dictLabel,
        String dictValue,
        Integer sortOrder,
        String listClass,
        Integer status,
        String remark) {

    public static DictDataResponse from(DictData entity) {
        return new DictDataResponse(
                entity.getId(),
                entity.getDictType(),
                entity.getDictLabel(),
                entity.getDictValue(),
                entity.getSortOrder(),
                entity.getListClass(),
                entity.getStatus(),
                entity.getRemark());
    }
}
