package com.ppmb.core.validation.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ppmb.core.validation.annotation.IdCard;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdCardValidatorTest {

    @Mock private IdCard idCardAnnotation;

    @Mock private ConstraintValidatorContext context;

    private IdCardValidator validator;

    @BeforeEach
    void setUp() {
        validator = new IdCardValidator();
    }

    @Test
    void testValidIdCards() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        // A valid test id card. Example: 11010519491231002X
        assertTrue(validator.isValid("11010519491231002X", context));
        // Another valid one: 34052419800101001X
        assertTrue(validator.isValid("34052419800101001X", context));
        // Test lower case x
        assertTrue(validator.isValid("11010519491231002x", context));
        // Real number test with digit ending: 440524188001010014
        assertTrue(validator.isValid("440524188001010014", context));
    }

    @Test
    void testInvalidIdCardFormat() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        assertFalse(validator.isValid("11010519491231002", context)); // length 17
        assertFalse(validator.isValid("A1010519491231002X", context)); // Starts with letter
    }

    @Test
    void testInvalidBirthDate() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        // 2023-02-30
        assertFalse(validator.isValid("110105202302300010", context));
        // 2023-13-01
        assertFalse(validator.isValid("110105202313010010", context));
        // 1800 year (regex reject 18)
        assertFalse(validator.isValid("110105179912310011", context));
    }

    @Test
    void testFutureBirthDate() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        // A future date, assuming current year is around 2024. Using 2099 to definitely fail.
        assertFalse(validator.isValid("11010520991231002X", context));
    }

    @Test
    void testInvalidCheckDigit() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        // Valid base but wrong check digit (expected X, testing 1)
        assertFalse(validator.isValid("110105194912310021", context));
    }

    @Test
    void testAllowBlankTrue() {
        when(idCardAnnotation.allowBlank()).thenReturn(true);
        validator.initialize(idCardAnnotation);

        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    void testAllowBlankFalse() {
        when(idCardAnnotation.allowBlank()).thenReturn(false);
        validator.initialize(idCardAnnotation);

        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }
}
