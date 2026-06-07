package com.ppmb.sys.file.application.job;

import com.ppmb.core.config.thread.ThreadPoolConfiguration;
import com.ppmb.core.storage.StorageService;
import com.ppmb.sys.file.domain.entity.SysFile;
import com.ppmb.sys.file.domain.model.FileStatus;
import com.ppmb.sys.file.domain.repository.SysFileRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SysFileCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(SysFileCleanupJob.class);

    private final StorageService storageService;
    private final SysFileRepository sysFileRepository;

    public SysFileCleanupJob(StorageService storageService, SysFileRepository sysFileRepository) {
        this.storageService = storageService;
        this.sysFileRepository = sysFileRepository;
    }

    /**
     * Periodically scans for files that are softly deleted (status = DELETED) but failed physical
     * removal, and retries the process. Uses virtual threads for asynchronous execution. Runs every
     * day at 3 AM.
     */
    @Async(ThreadPoolConfiguration.SYSTEM_LOG_EXECUTOR)
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupDeletedFiles() {
        log.info("Starting scheduled cleanup of orphaned files...");

        List<SysFile> deletedFiles = sysFileRepository.findByStatus(FileStatus.DELETED);
        int successCount = 0;
        int failureCount = 0;

        for (SysFile sysFile : deletedFiles) {
            try {
                // Retry physical deletion
                storageService.delete(sysFile.getBucketName(), sysFile.getObjectKey());
                log.info(
                        "Successfully recovered and physically deleted orphaned file object: {}/{}",
                        sysFile.getBucketName(),
                        sysFile.getObjectKey());

                // Perform hard deletion from database
                sysFileRepository.delete(sysFile);
                successCount++;
            } catch (Exception e) {
                log.error(
                        "Scheduled cleanup failed to physically delete file {}: {}",
                        sysFile.getId(),
                        e.getMessage());
                failureCount++;
            }
        }

        log.info(
                "Finished scheduled cleanup of orphaned files. Success: {}, Failed: {}",
                successCount,
                failureCount);
    }
}
