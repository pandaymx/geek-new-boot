package com.ppmb.core.log.presentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 操作日志注解，应用于表现层 Controller 方法。 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpLog {

    /** 业务模块名称 (e.g. "文件管理") */
    String title() default "";

    /** 操作类型 (e.g. "INSERT", "UPDATE", "DELETE") */
    String action() default "";
}
