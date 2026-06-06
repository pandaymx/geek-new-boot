package com.ppmb.core.validation.annotation;

import com.ppmb.core.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 强密码复杂度校验注解 规则：必须包含大小写字母、数字和特殊字符，长度8-20位 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "密码必须包含大小写字母、数字和特殊字符，且长度为8-20位";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 是否允许为空或空字符串。 默认为 false，即必须包含有效密码。 如果设置为 true，则输入为空、仅包含空格或 null 时将直接通过校验。 */
    boolean allowBlank() default false;
}
