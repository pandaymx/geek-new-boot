package com.ppmb.security.config;

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭 CSRF 防护（前后端分离项目或开发环境的标配）
                .csrf(csrf -> csrf.disable())

                // 2. 配置请求拦截策略
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        // 核心：利用 EndpointRequest.toAnyEndpoint() 匹配并放行所有的 Actuator
                                        // 端点
                                        .requestMatchers(EndpointRequest.toAnyEndpoint())
                                        .permitAll()

                                        // 放行 Swagger 相关路径
                                        .requestMatchers(
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/swagger-ui.html")
                                        .permitAll()

                                        // 其他所有请求依然保持原样，需要认证（或者你可以根据需求调整）
                                        .anyRequest()
                                        .authenticated())

                // 3. 保持原有的 Basic 认证能力（方便你用其他接口测试）
                .httpBasic(basic -> {});

        return http.build();
    }
}
