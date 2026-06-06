package com.ppmb.core.storage.s3;

import com.ppmb.core.storage.StorageService;
import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(S3StorageProperties.class)
@ConditionalOnProperty(prefix = "storage", name = "provider", havingValue = "s3")
public class S3StorageAutoConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public S3Client s3Client(S3StorageProperties properties) {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());

        S3ClientBuilder builder =
                S3Client.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(credentials))
                        .region(Region.of(properties.getRegion()))
                        .forcePathStyle(properties.isPathStyleAccessEnabled());

        if (properties.getEndpoint() != null && !properties.getEndpoint().isEmpty()) {
            builder.endpointOverride(URI.create(properties.getEndpoint()));
        }

        return builder.build();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public S3Presigner s3Presigner(S3StorageProperties properties) {
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());

        S3Presigner.Builder builder =
                S3Presigner.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(credentials))
                        .region(Region.of(properties.getRegion()));

        if (properties.getEndpoint() != null && !properties.getEndpoint().isEmpty()) {
            builder.endpointOverride(URI.create(properties.getEndpoint()));
        }

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean(StorageService.class)
    public StorageService s3StorageService(
            S3Client s3Client, S3Presigner s3Presigner, S3StorageProperties properties) {
        return new S3StorageService(s3Client, s3Presigner, properties);
    }
}
