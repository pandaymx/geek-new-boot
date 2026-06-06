package com.ppmb.core.storage.local;

import com.ppmb.core.storage.StorageException;
import com.ppmb.core.storage.StorageService;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageService.class);

    private final LocalStorageProperties properties;

    public LocalStorageService(LocalStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(
            InputStream inputStream, String bucketName, String objectName, String contentType) {
        try {
            // e.g. /data/uploads/avatar/2026/06/07/snowflakeId.png
            Path bucketPath = Paths.get(properties.getBasePath(), bucketName);
            Path filePath = bucketPath.resolve(objectName).normalize();

            // Security check: Prevent path traversal
            if (!filePath.startsWith(bucketPath)) {
                throw new StorageException("Path traversal attempt detected!");
            }

            // Create directories if they do not exist
            Files.createDirectories(filePath.getParent());

            // Write stream to file securely and efficiently using java.nio.file.Files
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File successfully written to local path: {}", filePath);

            // Return full absolute URL mapping to the static resource handler
            String url =
                    properties.getUrlPrefix().endsWith("/")
                            ? properties.getUrlPrefix()
                            : properties.getUrlPrefix() + "/";
            // objectName may contain paths like "2026/06/07/id.png", ensure proper URL construction
            String relativeUrlPath = bucketName + "/" + objectName;
            // Clean up any double slashes in the path
            relativeUrlPath = relativeUrlPath.replaceAll("//+", "/");

            return url + relativeUrlPath;
        } catch (Exception e) {
            log.error("Failed to store file in local storage", e);
            throw new StorageException("Failed to store file in local storage", e);
        }
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            Path bucketPath = Paths.get(properties.getBasePath(), bucketName);
            Path filePath = bucketPath.resolve(objectName).normalize();

            // Security check: Prevent path traversal
            if (!filePath.startsWith(bucketPath)) {
                throw new StorageException("Path traversal attempt detected!");
            }

            Files.deleteIfExists(filePath);
            log.info("File successfully deleted from local path: {}", filePath);
        } catch (Exception e) {
            log.error("Failed to delete file from local storage", e);
            throw new StorageException("Failed to delete file from local storage", e);
        }
    }

    @Override
    public String getProvider() {
        return "local";
    }
}
