package com.ppmb.core.storage.s3;

import com.ppmb.core.storage.StorageException;
import com.ppmb.core.storage.StorageService;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

public class S3StorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3StorageProperties properties;

    public S3StorageService(
            S3Client s3Client, S3Presigner s3Presigner, S3StorageProperties properties) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
    }

    @Override
    public String upload(
            InputStream inputStream, String bucketName, String objectName, String contentType) {
        try {
            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .contentType(contentType)
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, inputStream.available()));

            log.info("File successfully written to S3: {}/{}", bucketName, objectName);

            String urlPrefix = properties.getUrlPrefix();
            if (urlPrefix != null && !urlPrefix.isEmpty()) {
                String prefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
                return prefix + objectName;
            } else {
                // If no CDN prefix is configured, return the S3 internal key
                return bucketName + "/" + objectName;
            }

        } catch (Exception e) {
            log.error("Failed to store file in S3", e);
            throw new StorageException("Failed to store file in S3", e);
        }
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            DeleteObjectRequest deleteObjectRequest =
                    DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File successfully deleted from S3: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to delete file from S3", e);
            throw new StorageException("Failed to delete file from S3", e);
        }
    }

    @Override
    public String getProvider() {
        return "s3";
    }

    @Override
    public String generatePresignedUrl(String bucketName, String objectName, HttpMethod method) {
        try {
            if (method == HttpMethod.PUT) {
                PutObjectRequest putObjectRequest =
                        PutObjectRequest.builder().bucket(bucketName).key(objectName).build();
                PutObjectPresignRequest presignRequest =
                        PutObjectPresignRequest.builder()
                                .signatureDuration(properties.getExpiration())
                                .putObjectRequest(putObjectRequest)
                                .build();
                return s3Presigner.presignPutObject(presignRequest).url().toString();
            } else if (method == HttpMethod.GET) {
                GetObjectRequest getObjectRequest =
                        GetObjectRequest.builder().bucket(bucketName).key(objectName).build();
                GetObjectPresignRequest presignRequest =
                        GetObjectPresignRequest.builder()
                                .signatureDuration(properties.getExpiration())
                                .getObjectRequest(getObjectRequest)
                                .build();
                return s3Presigner.presignGetObject(presignRequest).url().toString();
            } else {
                throw new StorageException("Unsupported HTTP method for presigned URL: " + method);
            }
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for S3", e);
            throw new StorageException("Failed to generate presigned URL for S3", e);
        }
    }
}
