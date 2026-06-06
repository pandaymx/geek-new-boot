package com.ppmb.core.validation.validator;

import com.ppmb.core.validation.annotation.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/** 中国大陆18位身份证号码校验器 采用严格校验：正则表达式基础格式校验 + 真实的出生日期校验 + ISO 7064:1983.MOD 11-2 加权因子校验码校验 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    // 18位身份证基础正则校验
    private static final Pattern IDCARD_PATTERN =
            Pattern.compile(
                    "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])\\d{3}[0-9Xx]$");

    // 加权因子
    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    // 校验码映射
    private static final char[] CHECK_CODE_MAP = {
        '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'
    };

    // 日期格式化，使用STRICT模式进行严格日期解析（如拒绝 2023-02-30）
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT);

    private boolean allowBlank;

    @Override
    public void initialize(IdCard constraintAnnotation) {
        this.allowBlank = constraintAnnotation.allowBlank();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return allowBlank;
        }

        // 统一转换为大写处理 X
        String idCard = value.toUpperCase();

        // 1. 正则初步校验
        if (!IDCARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }

        // 2. 严格校验出生日期
        String dateStr = idCard.substring(6, 14);
        try {
            LocalDate birthDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            // 简单校验出生日期不能在未来
            if (birthDate.isAfter(LocalDate.now())) {
                return false;
            }
        } catch (DateTimeParseException e) {
            // 日期不合法（如 02-30）
            return false;
        }

        // 3. 校验码（第18位）校验
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard.charAt(i) - '0') * WEIGHTS[i];
        }

        int mod = sum % 11;
        char expectedCheckCode = CHECK_CODE_MAP[mod];
        char actualCheckCode = idCard.charAt(17);

        return expectedCheckCode == actualCheckCode;
    }
}
