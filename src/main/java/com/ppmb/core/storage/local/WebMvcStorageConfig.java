package com.ppmb.core.storage.local;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(LocalStorageProperties.class)
@ConditionalOnProperty(
        prefix = "storage",
        name = "provider",
        havingValue = "local",
        matchIfMissing = true)
public class WebMvcStorageConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebMvcStorageConfig.class);

    private final LocalStorageProperties properties;

    public WebMvcStorageConfig(LocalStorageProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(properties.getBasePath()).toAbsolutePath().normalize();
        String resourceLocation = "file:" + uploadDir.toString() + "/";
        log.info("Mapping local storage base path '{}' to resource handler", resourceLocation);

        registry.addResourceHandler("/uploads/**").addResourceLocations(resourceLocation);
    }
}
