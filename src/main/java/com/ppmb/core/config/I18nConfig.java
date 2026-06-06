package com.ppmb.core.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * 国际化配置
 *
 * <p>TODO: Integration with global exception handling: When BusinessException and
 * GlobalExceptionHandler (@RestControllerAdvice) are implemented, inject MessageSource into the
 * exception handler and resolve the message based on the exception's ErrorCode and
 * LocaleContextHolder.getLocale(). Result<T> should wrap this translated message.
 */
@Configuration
public class I18nConfig {

    /** 配置 LocaleResolver，支持从 X-Locale 自定义请求头或 Accept-Language 中获取区域信息。 默认降级为简体中文。 */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver =
                new AcceptHeaderLocaleResolver() {
                    @Override
                    public Locale resolveLocale(HttpServletRequest request) {
                        // 1. 优先尝试从自定义 Header 获取
                        String localeHeader = request.getHeader("X-Locale");
                        if (StringUtils.hasText(localeHeader)) {
                            try {
                                return StringUtils.parseLocaleString(localeHeader);
                            } catch (IllegalArgumentException e) {
                                // ignore and fallback to Accept-Language
                            }
                        }

                        // 2. 否则通过原始的 Accept-Language 提取
                        Locale defaultLocale = super.resolveLocale(request);
                        // 兜底降级策略：如果没有或者不支持，采用系统默认设置（在这里我们强制设置为 zh-CN）
                        if (defaultLocale == null) {
                            return Locale.SIMPLIFIED_CHINESE;
                        }
                        return defaultLocale;
                    }
                };
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }

    /** 将 Spring 的 MessageSource 注入到 Hibernate Validator，以便校验注解中可以使用占位符读取 i18n 配置。 */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
