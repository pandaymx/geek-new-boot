package com.ppmb.sys.file.presentation.dto;

import com.ppmb.sys.file.domain.entity.SysFile;

public record SysFileResponse(
        Long id,
        String bizType,
        String fileName,
        String originalName,
        String extension,
        Long fileSize,
        String fileUrl,
        String provider) {

    public static SysFileResponse from(SysFile entity) {
        return new SysFileResponse(
                entity.getId(),
                entity.getBizType(),
                entity.getFileName(),
                entity.getOriginalName(),
                entity.getExtension(),
                entity.getFileSize(),
                entity.getFileUrl(),
                entity.getProvider());
    }
}
