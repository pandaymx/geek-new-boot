package com.ppmb.core.infrastructure.id.config;

import com.ppmb.core.infrastructure.id.SnowflakeIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(IdGeneratorProperties.class) // 激活属性绑定
public class IdGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeIdGenerator snowflakeIdGenerator(IdGeneratorProperties properties) {
        // 从强类型的 properties 中获取配置值
        return new SnowflakeIdGenerator(properties.getWorkerId(), properties.getDataCenterId());
    }
}
