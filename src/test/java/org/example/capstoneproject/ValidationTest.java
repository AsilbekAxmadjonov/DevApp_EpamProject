package org.example.capstoneproject;

import org.example.capstoneproject.service.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    private Validation validation;

    @BeforeEach
    void setUp() {
        validation = new Validation();
    }

    @Test
    void validatePhoneNumber_valid() {
        assertTrue(validation.validatePhoneNumber("+998901234567")); // 13 chars, starts with +
    }

    @Test
    void validatePhoneNumber_invalidTooShort() {
        assertFalse(validation.validatePhoneNumber("+99890")); // too short
    }

    @Test
    void validatePhoneNumber_invalidNoPlus() {
        assertFalse(validation.validatePhoneNumber("998901234567")); // missing +
    }

    @Test
    void validatePhoneNumber_invalidContainsLetters() {
        assertFalse(validation.validatePhoneNumber("+99890abc4567")); // letters inside
    }

    // --------------------
    // Email tests
    // --------------------

    @Test
    void validateEmail_validSimple() {
        assertTrue(validation.validateEmail("user@example.com"));
    }

    @Test
    void validateEmail_validWithSpecials() {
        assertTrue(validation.validateEmail("first.last+tag@sub.domain.co"));
    }

    @Test
    void validateEmail_invalidNoAt() {
        assertFalse(validation.validateEmail("userexample.com"));
    }

    @Test
    void validateEmail_invalidNoDomain() {
        assertFalse(validation.validateEmail("user@"));
    }

    @Test
    void validateEmail_invalidEmpty() {
        assertFalse(validation.validateEmail(""));
    }
}
