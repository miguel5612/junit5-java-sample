package com.testing.example.unit;

import com.testing.example.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase User.
 * Demuestra el uso de JUnit 5 con @Nested, @DisplayName y assertions.
 */
@DisplayName("User Domain Tests")
class UserTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create user with valid parameters")
        void shouldCreateUserWithValidParameters() {
            // Given
            String id = "user-123";
            String email = "test@example.com";
            String name = "Test User";

            // When
            User user = new User(id, email, name);

            // Then
            assertAll("User properties",
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(name, user.getName()),
                () -> assertTrue(user.isActive()),
                () -> assertNotNull(user.getCreatedAt())
            );
        }

        @Test
        @DisplayName("Should create user with all parameters")
        void shouldCreateUserWithAllParameters() {
            // Given
            String id = "user-456";
            String email = "test2@example.com";
            String name = "Test User 2";
            LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
            boolean active = false;

            // When
            User user = new User(id, email, name, createdAt, active);

            // Then
            assertAll("User properties",
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(name, user.getName()),
                () -> assertFalse(user.isActive()),
                () -> assertEquals(createdAt, user.getCreatedAt())
            );
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate inactive user")
        void shouldActivateInactiveUser() {
            // Given
            User user = new User("1", "test@example.com", "Test",
                               LocalDateTime.now(), false);

            // When
            user.activate();

            // Then
            assertTrue(user.isActive());
        }

        @Test
        @DisplayName("Should deactivate active user")
        void shouldDeactivateActiveUser() {
            // Given
            User user = new User("1", "test@example.com", "Test");

            // When
            user.deactivate();

            // Then
            assertFalse(user.isActive());
        }

        @Test
        @DisplayName("Should allow multiple activations")
        void shouldAllowMultipleActivations() {
            // Given
            User user = new User("1", "test@example.com", "Test");

            // When
            user.deactivate();
            user.activate();
            user.activate();

            // Then
            assertTrue(user.isActive());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when IDs are the same")
        void shouldBeEqualWhenIdsAreSame() {
            // Given
            User user1 = new User("1", "test1@example.com", "User 1");
            User user2 = new User("1", "test2@example.com", "User 2");

            // Then
            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when IDs are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            // Given
            User user1 = new User("1", "test@example.com", "User 1");
            User user2 = new User("2", "test@example.com", "User 1");

            // Then
            assertNotEquals(user1, user2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            User user = new User("1", "test@example.com", "User");

            // Then
            assertEquals(user, user);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            User user = new User("1", "test@example.com", "User");

            // Then
            assertNotEquals(user, null);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should generate string representation")
        void shouldGenerateStringRepresentation() {
            // Given
            User user = new User("1", "test@example.com", "Test User");

            // When
            String result = user.toString();

            // Then - Using AssertJ for more expressive assertions
            assertThat(result)
                .contains("User{")
                .contains("id='1'")
                .contains("email='test@example.com'")
                .contains("name='Test User'")
                .contains("active=true");
        }
    }
}
