package com.ppmb.core.storage.local;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local-s3")
@ConditionalOnProperty(
        prefix = "storage",
        name = "provider",
        havingValue = "local",
        matchIfMissing = true)
public class LocalPresignedUploadController {

    private final LocalStorageService localStorageService;

    public LocalPresignedUploadController(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @PutMapping("/{bucketName}/**")
    public void upload(@PathVariable String bucketName, HttpServletRequest request)
            throws Exception {

        // Extract objectName from URI (everything after bucketName/)
        String path = request.getRequestURI();
        String bucketPrefix = "/local-s3/" + bucketName + "/";
        String objectName = path.substring(path.indexOf(bucketPrefix) + bucketPrefix.length());

        try (InputStream inputStream = request.getInputStream()) {
            localStorageService.upload(
                    inputStream, bucketName, objectName, request.getContentType());
        }
    }
}
