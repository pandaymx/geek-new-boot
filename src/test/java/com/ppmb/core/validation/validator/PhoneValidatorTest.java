package com.ppmb.core.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ppmb.core.validation.annotation.Phone;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhoneValidatorTest {

    @Mock private Phone phoneAnnotation;

    @Mock private ConstraintValidatorContext context;

    private PhoneValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhoneValidator();
    }

    @Test
    void testValidPhoneNumbers() {
        when(phoneAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(phoneAnnotation);

        assertTrue(validator.isValid("13812345678", context));
        assertTrue(validator.isValid("19912345678", context));
        assertTrue(validator.isValid("14712345678", context));
    }

    @Test
    void testInvalidPhoneNumbers() {
        when(phoneAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(phoneAnnotation);

        assertFalse(validator.isValid("12812345678", context)); // starts with 12
        assertFalse(validator.isValid("1381234567", context)); // 10 digits
        assertFalse(validator.isValid("138123456789", context)); // 12 digits
        assertFalse(validator.isValid("1381234567a", context)); // contains letter
    }

    @Test
    void testAllowBlankTrue() {
        when(phoneAnnotation.allowBlank()).thenReturn(true);
        validator.initialize(phoneAnnotation);

        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
        assertTrue(validator.isValid("13812345678", context)); // Valid still true
    }

    @Test
    void testAllowBlankFalse() {
        when(phoneAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(phoneAnnotation);

        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }
}
