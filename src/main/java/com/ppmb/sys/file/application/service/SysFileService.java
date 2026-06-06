package com.ppmb.sys.file.application.service;

import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import com.ppmb.core.storage.StorageService;
import com.ppmb.sys.file.domain.entity.SysFile;
import com.ppmb.sys.file.domain.repository.SysFileRepository;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysFileService {

    private static final Logger log = LoggerFactory.getLogger(SysFileService.class);

    private final StorageService storageService;
    private final SysFileRepository sysFileRepository;
    private final SnowflakeIdGenerator idGenerator;

    public SysFileService(
            StorageService storageService,
            SysFileRepository sysFileRepository,
            SnowflakeIdGenerator idGenerator) {
        this.storageService = storageService;
        this.sysFileRepository = sysFileRepository;
        this.idGenerator = idGenerator;
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
                        bucketName);

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
                            // Delete physically via StorageService
                            try {
                                // Object name was stored indirectly, reconstruct it based on
                                // original generation logic
                                // Or simply pass what is needed. If we don't have objectName
                                // explicitly stored,
                                // we reconstruct it or extract from URL if possible.
                                // Based on our saving strategy: the relative path inside bucket is
                                // needed.
                                // We should parse the URL or reconstruct.
                                // For simplicity, let's reconstruct it. Since we partition by date,
                                // we might not know it
                                // perfectly unless we store the relative path.
                                // Let's refine: actually `bucketName` was stored. We can extract
                                // the relative path from the URL.
                                String relativePath =
                                        extractRelativePath(
                                                sysFile.getFileUrl(), sysFile.getBucketName());
                                storageService.delete(sysFile.getBucketName(), relativePath);
                            } catch (Exception e) {
                                log.error(
                                        "Failed to physically delete file {}: {}",
                                        fileId,
                                        e.getMessage());
                                // In a robust system, we might queue this for retry or ignore based
                                // on business needs.
                                // Proceeding to delete metadata anyway for now.
                            }

                            // Delete metadata
                            sysFileRepository.delete(sysFile);
                            log.info("File metadata deleted for id: {}", fileId);
                        });
    }

    private String extractRelativePath(String fileUrl, String bucketName) {
        // e.g. URL: http://localhost:8080/uploads/avatar/2026/06/07/123.png
        // We need: 2026/06/07/123.png
        // We know bucketName is "avatar"
        String searchStr = "/" + bucketName + "/";
        int index = fileUrl.indexOf(searchStr);
        if (index != -1) {
            return fileUrl.substring(index + searchStr.length());
        }
        // Fallback: If we can't extract cleanly, just return filename (might fail deletion)
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }
}
