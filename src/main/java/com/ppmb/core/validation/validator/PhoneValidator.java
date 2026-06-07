package com.ppmb.core.validation.validator;

import com.ppmb.core.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/** 手机号校验器 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    // 中国大陆手机号正则匹配：13、14、15、16、17、18、19 开头，共 11 位
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private boolean allowBlank;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return allowBlank;
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}
