package com.ppmb.sys.file.domain.entity;

import com.ppmb.core.domain.base.AbstractEntity;
import com.ppmb.core.domain.base.Auditable;
import com.ppmb.core.domain.base.model.AuditInfo;
import com.ppmb.sys.file.domain.model.FileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_file")
public class SysFile extends AbstractEntity<Long> implements Auditable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "biz_type", length = 50)
    private String bizType;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;

    @Column(name = "extension", length = 10, nullable = false)
    private String extension;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_url", length = 512, nullable = false)
    private String fileUrl;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name = "bucket_name", length = 100)
    private String bucketName;

    @Column(name = "object_key", length = 512, nullable = false)
    private String objectKey;

    @Column(name = "status", nullable = false)
    private FileStatus status;

    @Embedded private AuditInfo auditInfo = AuditInfo.empty();

    protected SysFile() {
        // JPA requires a protected no-arg constructor
    }

    public SysFile(
            Long id,
            String bizType,
            String fileName,
            String originalName,
            String extension,
            Long fileSize,
            String contentType,
            String fileUrl,
            String provider,
            String bucketName,
            String objectKey,
            FileStatus status) {
        this.id = id;
        this.bizType = bizType;
        this.fileName = fileName;
        this.originalName = originalName;
        this.extension = extension;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.fileUrl = fileUrl;
        this.provider = provider;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    @Override
    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getBizType() {
        return bizType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getExtension() {
        return extension;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getProvider() {
        return provider;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }
}
