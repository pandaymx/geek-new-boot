package com.ppmb.sys.file.domain.event;

import org.springframework.context.ApplicationEvent;

public class FileDeletedEvent extends ApplicationEvent {

    private final Long fileId;
    private final String bucketName;
    private final String objectKey;

    public FileDeletedEvent(Object source, Long fileId, String bucketName, String objectKey) {
        super(source);
        this.fileId = fileId;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }
}
