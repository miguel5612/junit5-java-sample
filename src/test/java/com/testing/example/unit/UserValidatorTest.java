package com.testing.example.unit;

import com.testing.example.domain.User;
import com.testing.example.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para UserValidator.
 * Demuestra técnicas de testing para validaciones.
 */
@DisplayName("UserValidator Tests")
class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    @Nested
    @DisplayName("Valid User Tests")
    class ValidUserTests {

        @Test
        @DisplayName("Should validate correct user")
        void shouldValidateCorrectUser() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertTrue(errors.isEmpty());
            assertTrue(validator.isValid(user));
        }

        @Test
        @DisplayName("Should validate user with minimum name length")
        void shouldValidateUserWithMinimumNameLength() {
            // Given
            User user = new User("1", "test@example.com", "AB");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate user with long name")
        void shouldValidateUserWithLongName() {
            // Given
            String longName = "A".repeat(100);
            User user = new User("1", "test@example.com", longName);

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors).isEmpty();
        }
    }

    @Nested
    @DisplayName("Invalid User Tests")
    class InvalidUserTests {

        @Test
        @DisplayName("Should reject null user")
        void shouldRejectNullUser() {
            // When
            List<String> errors = validator.validate(null);

            // Then
            assertThat(errors)
                .hasSize(1)
                .contains("User cannot be null");
            assertFalse(validator.isValid(null));
        }

        @Test
        @DisplayName("Should reject null ID")
        void shouldRejectNullId() {
            // Given
            User user = new User(null, "test@example.com", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.contains("ID"));
        }

        @Test
        @DisplayName("Should reject empty ID")
        void shouldRejectEmptyId() {
            // Given
            User user = new User("  ", "test@example.com", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.contains("ID"));
        }
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        @Test
        @DisplayName("Should reject null email")
        void shouldRejectNullEmail() {
            // Given
            User user = new User("1", null, "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.toLowerCase().contains("email"));
        }

        @Test
        @DisplayName("Should reject empty email")
        void shouldRejectEmptyEmail() {
            // Given
            User user = new User("1", "", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.toLowerCase().contains("email"));
        }

        @Test
        @DisplayName("Should reject invalid email format")
        void shouldRejectInvalidEmailFormat() {
            // Given
            User user = new User("1", "invalid-email", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.contains("format"));
        }

        @Test
        @DisplayName("Should reject email without domain")
        void shouldRejectEmailWithoutDomain() {
            // Given
            User user = new User("1", "test@", "John Doe");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.toLowerCase().contains("email"));
        }

        @Test
        @DisplayName("Should accept valid email formats")
        void shouldAcceptValidEmailFormats() {
            // Given
            String[] validEmails = {
                "test@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "test_123@test-domain.com"
            };

            // Then
            for (String email : validEmails) {
                User user = new User("1", email, "John Doe");
                List<String> errors = validator.validate(user);
                assertThat(errors)
                    .as("Email %s should be valid", email)
                    .isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Name Validation Tests")
    class NameValidationTests {

        @Test
        @DisplayName("Should reject null name")
        void shouldRejectNullName() {
            // Given
            User user = new User("1", "test@example.com", null);

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.toLowerCase().contains("name"));
        }

        @Test
        @DisplayName("Should reject empty name")
        void shouldRejectEmptyName() {
            // Given
            User user = new User("1", "test@example.com", "");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.toLowerCase().contains("name"));
        }

        @Test
        @DisplayName("Should reject too short name")
        void shouldRejectTooShortName() {
            // Given
            User user = new User("1", "test@example.com", "A");

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.contains("at least"));
        }

        @Test
        @DisplayName("Should reject too long name")
        void shouldRejectTooLongName() {
            // Given
            String tooLongName = "A".repeat(101);
            User user = new User("1", "test@example.com", tooLongName);

            // When
            List<String> errors = validator.validate(user);

            // Then
            assertThat(errors)
                .isNotEmpty()
                .anyMatch(error -> error.contains("exceed"));
        }
    }

    @Nested
    @DisplayName("Multiple Errors Tests")
    class MultipleErrorsTests {

        @Test
        @DisplayName("Should collect all validation errors")
        void shouldCollectAllValidationErrors() {
            // Given - user with multiple issues
            User user = new User("", "invalid-email", "A");

            // When
            List<String> errors = validator.validate(user);

            // Then - should have errors for ID, email, and name
            assertThat(errors)
                .hasSizeGreaterThanOrEqualTo(3)
                .anyMatch(error -> error.toLowerCase().contains("id"))
                .anyMatch(error -> error.toLowerCase().contains("email"))
                .anyMatch(error -> error.toLowerCase().contains("name"));
        }
    }
}
