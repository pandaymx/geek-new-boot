package com.ppmb.core.validation.validator;

import com.ppmb.core.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/** 强密码复杂度校验器 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    // 强密码正则表达式：必须包含大小写字母、数字、特殊字符，且长度8-20位
    // 特殊字符包括常用的字符，可根据需要调整
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#\\-+=]).{8,20}$");

    private boolean allowBlank;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return allowBlank;
        }

        return PASSWORD_PATTERN.matcher(value).matches();
    }
}
