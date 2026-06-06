package com.ppmb.core.storage.local;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage.local")
public class LocalStorageProperties {

    /** The physical base directory for local storage (e.g., /data/uploads). */
    private String basePath = "uploads";

    /** The URL prefix for accessing the files (e.g., http://localhost:8080/uploads/). */
    private String urlPrefix = "http://localhost:8080/uploads/";

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
