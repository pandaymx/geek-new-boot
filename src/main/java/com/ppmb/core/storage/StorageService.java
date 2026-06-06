package com.ppmb.core.storage;

import java.io.InputStream;
import org.springframework.http.HttpMethod;

/** Unified storage abstraction interface. Responsible for physical file storage operations. */
public interface StorageService {

    /**
     * Upload a file stream to the storage provider.
     *
     * @param inputStream The input stream of the file content
     * @param bucketName The bucket or root directory name
     * @param objectName The unique physical name (e.g. SnowflakeId.extension) including any
     *     time-based partitions
     * @param contentType The MIME type of the file
     * @return The absolute access URL or relative path depending on implementation
     */
    String upload(
            InputStream inputStream, String bucketName, String objectName, String contentType);

    /**
     * Delete a file from the storage provider.
     *
     * @param bucketName The bucket or root directory name
     * @param objectName The unique physical name including paths
     */
    void delete(String bucketName, String objectName);

    /**
     * Get the identifier of the storage provider (e.g., "local", "minio", "s3").
     *
     * @return provider identifier
     */
    String getProvider();


    /**
     * Generate a presigned URL for direct access to the storage provider.
     *
     * @param bucketName The bucket or root directory name
     * @param objectName The unique physical name
     * @param method The HTTP method for the presigned URL (e.g. GET, PUT)
     * @return The presigned URL
     */
    String generatePresignedUrl(String bucketName, String objectName, HttpMethod method);
}
