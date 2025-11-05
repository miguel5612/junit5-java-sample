package com.testing.example.parameterized;

import com.testing.example.domain.User;
import com.testing.example.util.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Ejemplos de tests dinámicos usando @TestFactory.
 * Los tests dinámicos se generan en tiempo de ejecución, permitiendo mayor flexibilidad.
 */
@DisplayName("Dynamic Tests Examples")
class DynamicTestsExample {

    private final UserValidator validator = new UserValidator();

    @TestFactory
    @DisplayName("Dynamically generate email validation tests")
    Collection<DynamicTest> dynamicEmailValidationTests() {
        // Given - lista de emails para testear
        List<String> validEmails = Arrays.asList(
            "test1@example.com",
            "test2@example.com",
            "test3@example.com",
            "user@domain.co.uk",
            "admin@company.org"
        );

        // When & Then - generar un test dinámico por cada email
        return validEmails.stream()
            .map(email -> dynamicTest(
                "Testing email: " + email,
                () -> {
                    User user = new User("1", email, "Test User");
                    assertTrue(validator.isValid(user), "Email " + email + " should be valid");
                }
            ))
            .toList();
    }

    @TestFactory
    @DisplayName("Dynamically generate name length validation tests")
    Stream<DynamicTest> dynamicNameLengthTests() {
        // Given - diferentes longitudes de nombre
        return Stream.of(1, 2, 5, 10, 50, 100, 101)
            .map(length -> dynamicTest(
                "Testing name with length: " + length,
                () -> {
                    String name = "A".repeat(length);
                    User user = new User("1", "test@example.com", name);
                    List<String> errors = validator.validate(user);

                    if (length < 2 || length > 100) {
                        assertThat(errors)
                            .as("Name with length %d should be invalid", length)
                            .isNotEmpty();
                    } else {
                        assertThat(errors)
                            .as("Name with length %d should be valid", length)
                            .isEmpty();
                    }
                }
            ));
    }

    @TestFactory
    @DisplayName("Dynamically generate user validation tests with multiple scenarios")
    Collection<DynamicTest> dynamicUserValidationTests() {
        // Given - diferentes escenarios de usuarios
        record TestScenario(String description, User user, boolean shouldBeValid) {}

        List<TestScenario> scenarios = Arrays.asList(
            new TestScenario(
                "Valid user with all correct data",
                new User("1", "john@example.com", "John Doe"),
                true
            ),
            new TestScenario(
                "User with invalid email format",
                new User("2", "invalid-email", "Jane Doe"),
                false
            ),
            new TestScenario(
                "User with too short name",
                new User("3", "test@example.com", "A"),
                false
            ),
            new TestScenario(
                "User with minimum valid name length",
                new User("4", "test@example.com", "AB"),
                true
            ),
            new TestScenario(
                "User with maximum valid name length",
                new User("5", "test@example.com", "A".repeat(100)),
                true
            ),
            new TestScenario(
                "User with too long name",
                new User("6", "test@example.com", "A".repeat(101)),
                false
            )
        );

        // When & Then - generar tests dinámicos
        return scenarios.stream()
            .map(scenario -> dynamicTest(
                scenario.description,
                () -> {
                    boolean isValid = validator.isValid(scenario.user);
                    assertThat(isValid)
                        .as(scenario.description)
                        .isEqualTo(scenario.shouldBeValid);
                }
            ))
            .toList();
    }

    @TestFactory
    @DisplayName("Dynamically generate tests for special characters in emails")
    Stream<DynamicTest> dynamicSpecialCharactersInEmails() {
        // Given - emails con caracteres especiales
        record EmailTest(String email, boolean shouldBeValid) {}

        List<EmailTest> emailTests = Arrays.asList(
            new EmailTest("user+tag@example.com", true),
            new EmailTest("user.name@example.com", true),
            new EmailTest("user_name@example.com", true),
            new EmailTest("user-name@example.com", true),
            new EmailTest("user@sub.domain.example.com", true),
            new EmailTest("user name@example.com", false),  // space
            new EmailTest("user@domain .com", false),  // space
            new EmailTest("user..double@example.com", false)  // double dot
        );

        return emailTests.stream()
            .map(test -> dynamicTest(
                "Email '" + test.email + "' should be " + (test.shouldBeValid ? "valid" : "invalid"),
                () -> {
                    User user = new User("1", test.email, "Test User");
                    boolean isValid = validator.isValid(user);
                    assertThat(isValid)
                        .as("Email '%s' validation", test.email)
                        .isEqualTo(test.shouldBeValid);
                }
            ));
    }

    @TestFactory
    @DisplayName("Dynamically generate boundary tests")
    Collection<DynamicTest> dynamicBoundaryTests() {
        return Arrays.asList(
            dynamicTest("Test minimum name boundary - 1 char (invalid)", () -> {
                User user = new User("1", "test@example.com", "A");
                assertThat(validator.isValid(user)).isFalse();
            }),
            dynamicTest("Test minimum name boundary - 2 chars (valid)", () -> {
                User user = new User("1", "test@example.com", "AB");
                assertThat(validator.isValid(user)).isTrue();
            }),
            dynamicTest("Test maximum name boundary - 100 chars (valid)", () -> {
                User user = new User("1", "test@example.com", "A".repeat(100));
                assertThat(validator.isValid(user)).isTrue();
            }),
            dynamicTest("Test maximum name boundary - 101 chars (invalid)", () -> {
                User user = new User("1", "test@example.com", "A".repeat(101));
                assertThat(validator.isValid(user)).isFalse();
            })
        );
    }
}
