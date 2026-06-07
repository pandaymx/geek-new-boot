package com.ppmb.sys.file.application.service;

import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.core.storage.StorageService;
import com.ppmb.sys.file.domain.entity.SysFile;
import com.ppmb.sys.file.domain.event.FileDeletedEvent;
import com.ppmb.sys.file.domain.model.FileStatus;
import com.ppmb.sys.file.domain.repository.SysFileRepository;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysFileService {

    private static final Logger log = LoggerFactory.getLogger(SysFileService.class);

    private final StorageService storageService;
    private final SysFileRepository sysFileRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final ApplicationEventPublisher eventPublisher;

    public SysFileService(
            StorageService storageService,
            SysFileRepository sysFileRepository,
            SnowflakeIdGenerator idGenerator,
            ApplicationEventPublisher eventPublisher) {
        this.storageService = storageService;
        this.sysFileRepository = sysFileRepository;
        this.idGenerator = idGenerator;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles file upload processing, invoking the storage layer for physical storage, and
     * recording file metadata in the database.
     *
     * @param inputStream The input stream of the uploaded file
     * @param originalName The original file name from the user
     * @param contentType The MIME type of the file
     * @param size The size of the file in bytes
     * @param bizType The business type tag (e.g. avatar, thesis_attachment)
     * @return The saved SysFile metadata
     */
    @Transactional
    public SysFile uploadFile(
            InputStream inputStream,
            String originalName,
            String contentType,
            long size,
            String bizType) {

        long fileId = idGenerator.nextId();

        // Extract extension safely
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < originalName.length() - 1) {
            extension = originalName.substring(dotIndex + 1).toLowerCase();
        }

        // Generate strict physical filename to avoid collisions and path traversal
        String physicalFileName = fileId + (extension.isEmpty() ? "" : "." + extension);

        // Define a bucket/root path and partition based on bizType and current date
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String bucketName = bizType == null || bizType.isBlank() ? "default" : bizType;

        // Object name incorporates the time partition to prevent deep directory structures
        String objectName = datePath + "/" + physicalFileName;

        // 1. Physically upload file via StorageService abstraction
        String url = storageService.upload(inputStream, bucketName, objectName, contentType);

        // 2. Save metadata via SysFileRepository
        SysFile sysFile =
                new SysFile(
                        fileId,
                        bizType,
                        physicalFileName,
                        originalName,
                        extension,
                        size,
                        contentType,
                        url,
                        storageService.getProvider(),
                        bucketName,
                        objectName,
                        FileStatus.NORMAL);

        return sysFileRepository.save(sysFile);
    }

    /**
     * Handles file deletion processing, deleting from both the physical storage layer and the
     * metadata from the database.
     *
     * @param fileId The ID of the file to delete
     */
    @Transactional
    public void deleteFile(Long fileId) {
        sysFileRepository
                .findById(fileId)
                .ifPresent(
                        sysFile -> {
                            // Perform soft deletion
                            sysFile.setStatus(FileStatus.DELETED);
                            sysFileRepository.save(sysFile);

                            // Publish domain event for physical deletion (asynchronous)
                            eventPublisher.publishEvent(
                                    new FileDeletedEvent(
                                            this,
                                            sysFile.getId(),
                                            sysFile.getBucketName(),
                                            sysFile.getObjectKey()));

                            log.info(
                                    "File metadata marked as DELETED for id: {}, pending physical removal.",
                                    fileId);
                        });
    }
}
