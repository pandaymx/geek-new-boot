package com.ppmb.sys.file.presentation.controller;

import com.ppmb.core.presentation.response.Result;
import com.ppmb.sys.file.application.service.SysFileService;
import com.ppmb.sys.file.domain.entity.SysFile;
import com.ppmb.sys.file.presentation.dto.SysFileResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sys/files")
public class SysFileController {

    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    private final SysFileService sysFileService;

    public SysFileController(SysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }

    /**
     * Upload a new file.
     *
     * @param file The file to upload
     * @param bizType The business type classification (optional)
     * @return Result containing the file metadata
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<SysFileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bizType", required = false, defaultValue = "default")
                    String bizType) {

        try {
            SysFile sysFile =
                    sysFileService.uploadFile(
                            file.getInputStream(),
                            file.getOriginalFilename() != null
                                    ? file.getOriginalFilename()
                                    : "unknown",
                            file.getContentType(),
                            file.getSize(),
                            bizType);
            return Result.success(SysFileResponse.from(sysFile));
        } catch (IOException e) {
            log.error("Error reading uploaded file", e);
            throw new RuntimeException("Error reading uploaded file", e);
        }
    }

    /**
     * Delete an existing file by its ID.
     *
     * @param id The ID of the file to delete
     * @return Success result
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteFile(@PathVariable Long id) {
        sysFileService.deleteFile(id);
        return Result.success(null);
    }
}
