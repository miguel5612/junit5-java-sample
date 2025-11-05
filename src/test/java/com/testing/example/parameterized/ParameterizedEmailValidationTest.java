package com.testing.example.parameterized;

import com.testing.example.domain.User;
import com.testing.example.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests parametrizados usando JUnit 5 @ParameterizedTest.
 * Demuestra diferentes fuentes de parámetros:
 * - @ValueSource
 * - @CsvSource
 * - @MethodSource
 * - @EnumSource
 */
@DisplayName("Parameterized Email Validation Tests")
class ParameterizedEmailValidationTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    @ParameterizedTest(name = "Email ''{0}'' should be valid")
    @DisplayName("Should accept valid email formats")
    @ValueSource(strings = {
        "test@example.com",
        "user.name@example.com",
        "user+tag@example.co.uk",
        "firstname.lastname@example.com",
        "email@subdomain.example.com",
        "firstname+lastname@example.com",
        "1234567890@example.com",
        "_______@example.com",
        "email@example-one.com",
        "email@example.name",
        "email@example.museum",
        "email@example.co.jp"
    })
    void shouldAcceptValidEmailFormats(String email) {
        // Given
        User user = new User("1", email, "Test User");

        // When
        List<String> errors = validator.validate(user);

        // Then
        assertThat(errors)
            .as("Email %s should be valid", email)
            .isEmpty();
    }

    @ParameterizedTest(name = "Email ''{0}'' should be invalid")
    @DisplayName("Should reject invalid email formats")
    @ValueSource(strings = {
        "invalid",
        "invalid@",
        "@invalid.com",
        "invalid@domain",
        "invalid @example.com",
        "invalid@domain .com",
        "",
        "   ",
        "invalid..email@example.com",
        "invalid@.com"
    })
    void shouldRejectInvalidEmailFormats(String email) {
        // Given
        User user = new User("1", email, "Test User");

        // When
        List<String> errors = validator.validate(user);

        // Then
        assertThat(errors)
            .as("Email %s should be invalid", email)
            .isNotEmpty()
            .anyMatch(error -> error.toLowerCase().contains("email"));
    }

    @ParameterizedTest(name = "User with name=''{0}'', email=''{1}'' should be {2}")
    @DisplayName("Should validate users with various combinations")
    @CsvSource({
        "John Doe, john@example.com, valid",
        "AB, test@example.com, valid",
        "A, test@example.com, invalid",  // name too short
        "Valid Name, invalid-email, invalid",  // invalid email
        "'', test@example.com, invalid",  // empty name
        "Valid Name, '', invalid"  // empty email
    })
    void shouldValidateUsersWithVariousCombinations(String name, String email, String expectedResult) {
        // Given
        User user = new User("1", email, name);

        // When
        boolean isValid = validator.isValid(user);

        // Then
        if ("valid".equals(expectedResult)) {
            assertTrue(isValid, String.format("User with name='%s', email='%s' should be valid", name, email));
        } else {
            assertThat(isValid)
                .as("User with name='%s', email='%s' should be invalid", name, email)
                .isFalse();
        }
    }

    @ParameterizedTest(name = "Name length {0} should be {1}")
    @DisplayName("Should validate name lengths")
    @CsvSource({
        "1, invalid",
        "2, valid",
        "50, valid",
        "100, valid",
        "101, invalid"
    })
    void shouldValidateNameLengths(int nameLength, String expectedResult) {
        // Given
        String name = "A".repeat(nameLength);
        User user = new User("1", "test@example.com", name);

        // When
        boolean isValid = validator.isValid(user);

        // Then
        if ("valid".equals(expectedResult)) {
            assertTrue(isValid, "Name with length " + nameLength + " should be valid");
        } else {
            assertThat(isValid)
                .as("Name with length %d should be invalid", nameLength)
                .isFalse();
        }
    }

    @ParameterizedTest
    @DisplayName("Should validate users from method source")
    @MethodSource("provideUsersForValidation")
    void shouldValidateUsersFromMethodSource(User user, boolean expectedValid) {
        // When
        boolean isValid = validator.isValid(user);

        // Then
        assertThat(isValid).isEqualTo(expectedValid);
    }

    /**
     * Method source that provides test data for parameterized tests.
     */
    private static Stream<Arguments> provideUsersForValidation() {
        return Stream.of(
            Arguments.of(new User("1", "valid@example.com", "Valid Name"), true),
            Arguments.of(new User("2", "another@test.com", "Another User"), true),
            Arguments.of(new User("3", "invalid-email", "Name"), false),
            Arguments.of(new User("4", "test@example.com", "A"), false),
            Arguments.of(new User("5", "", "Name"), false),
            Arguments.of(new User("6", "test@example.com", ""), false)
        );
    }

    @ParameterizedTest(name = "Domain {0} should create valid email")
    @DisplayName("Should validate emails with different domains")
    @MethodSource("provideDomains")
    void shouldValidateEmailsWithDifferentDomains(String domain) {
        // Given
        String email = "test@" + domain;
        User user = new User("1", email, "Test User");

        // When
        boolean isValid = validator.isValid(user);

        // Then
        assertTrue(isValid, "Email with domain " + domain + " should be valid");
    }

    private static Stream<String> provideDomains() {
        return Stream.of(
            "example.com",
            "test-domain.com",
            "subdomain.example.com",
            "example.co.uk",
            "example.org",
            "example.net",
            "long-domain-name.example.com"
        );
    }

    @ParameterizedTest
    @DisplayName("Should handle boundary cases")
    @CsvSource(delimiter = '|', value = {
        "1|min-invalid",
        "2|min-valid",
        "100|max-valid",
        "101|max-invalid"
    })
    void shouldHandleBoundaryCases(int nameLength, String category) {
        // Given
        String name = "A".repeat(nameLength);
        User user = new User("1", "test@example.com", name);

        // When
        List<String> errors = validator.validate(user);

        // Then
        if (category.contains("valid")) {
            assertThat(errors)
                .as("Name length %d should be valid", nameLength)
                .isEmpty();
        } else {
            assertThat(errors)
                .as("Name length %d should be invalid", nameLength)
                .isNotEmpty();
        }
    }
}
