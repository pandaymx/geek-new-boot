package com.ppmb.sys.file.application.listener;

import com.ppmb.core.config.thread.ThreadPoolConfiguration;
import com.ppmb.core.storage.StorageService;
import com.ppmb.sys.file.domain.event.FileDeletedEvent;
import com.ppmb.sys.file.domain.repository.SysFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SysFileEventListener {

    private static final Logger log = LoggerFactory.getLogger(SysFileEventListener.class);

    private final StorageService storageService;
    private final SysFileRepository sysFileRepository;

    public SysFileEventListener(
            StorageService storageService, SysFileRepository sysFileRepository) {
        this.storageService = storageService;
        this.sysFileRepository = sysFileRepository;
    }

    @Async(ThreadPoolConfiguration.SYSTEM_LOG_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFileDeletedEvent(FileDeletedEvent event) {
        try {
            // Delete physically via StorageService
            storageService.delete(event.getBucketName(), event.getObjectKey());
            log.info(
                    "Physically deleted file object: {}/{}",
                    event.getBucketName(),
                    event.getObjectKey());

            // Delete metadata from DB now that physical file is gone
            sysFileRepository.deleteById(event.getFileId());
            log.info("File metadata permanently deleted for id: {}", event.getFileId());

        } catch (Exception e) {
            log.error("Failed to physically delete file {}: {}", event.getFileId(), e.getMessage());
            // It will remain in DELETED state in DB, picked up by scheduled job later
        }
    }
}
