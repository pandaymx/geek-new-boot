package com.ppmb.core.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ppmb.core.validation.annotation.Password;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Mock private Password passwordAnnotation;

    @Mock private ConstraintValidatorContext context;

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    void testValidPasswords() {
        when(passwordAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(passwordAnnotation);

        assertTrue(validator.isValid("Aa1!abcd", context));
        assertTrue(validator.isValid("StrongPass123@", context));
        assertTrue(validator.isValid("P@ssw0rd1234567890", context)); // 20 chars
    }

    @Test
    void testInvalidPasswords() {
        when(passwordAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(passwordAnnotation);

        assertFalse(validator.isValid("Aa1!abc", context)); // length 7 (too short)
        assertFalse(validator.isValid("Aa1!abcdefghijklmnopqr", context)); // length 22 (too long)
        assertFalse(validator.isValid("aa1!abcd", context)); // no uppercase
        assertFalse(validator.isValid("AA1!ABCD", context)); // no lowercase
        assertFalse(validator.isValid("Aaaaa!abcd", context)); // no digit
        assertFalse(validator.isValid("Aa11abcd", context)); // no special char
    }

    @Test
    void testAllowBlankTrue() {
        when(passwordAnnotation.allowBlank()).thenReturn(true);
        validator.initialize(passwordAnnotation);

        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    void testAllowBlankFalse() {
        when(passwordAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(passwordAnnotation);

        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }
}
