package com.ppmb.core.storage.local;

import com.ppmb.core.storage.StorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LocalStorageProperties.class)
@ConditionalOnProperty(
        prefix = "storage",
        name = "provider",
        havingValue = "local",
        matchIfMissing = true)
public class StorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    public LocalStorageService localStorageService(LocalStorageProperties properties) {
        return new LocalStorageService(properties);
    }
}
