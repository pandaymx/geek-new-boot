package com.ppmb.core.validation.annotation;

import com.ppmb.core.validation.validator.PhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 手机号校验注解 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,
    ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

    String message() default "{validation.phone.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** 是否允许为空或空字符串。 默认为 false，即必须包含有效的手机号码。 如果设置为 true，则输入为空、仅包含空格或 null 时将直接通过校验。 */
    boolean allowBlank() default false;
}
