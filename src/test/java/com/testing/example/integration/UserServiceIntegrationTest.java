package com.testing.example.integration;

import com.testing.example.domain.User;
import com.testing.example.repository.InMemoryUserRepository;
import com.testing.example.service.*;
import com.testing.example.util.UserValidator;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para UserService.
 * Usa implementaciones reales de todas las dependencias.
 * Demuestra testing de flujos completos end-to-end.
 */
@DisplayName("UserService Integration Tests")
class UserServiceIntegrationTest {

    private UserService userService;
    private InMemoryUserRepository repository;
    private UserValidator validator;
    private TestNotifier notifier;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        validator = new UserValidator();
        notifier = new TestNotifier();
        userService = new UserService(repository, validator, notifier);
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Nested
    @DisplayName("User Registration Tests")
    class UserRegistrationTests {

        @Test
        @DisplayName("Should register valid user successfully")
        void shouldRegisterValidUserSuccessfully() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            // When
            User registered = userService.registerUser(user);

            // Then
            assertNotNull(registered);
            assertEquals(user.getId(), registered.getId());
            assertEquals(1, userService.countUsers());

            // Verify notification was sent
            assertThat(notifier.getSentNotifications())
                .hasSize(1)
                .anyMatch(notif ->
                    notif.contains("test@example.com") &&
                    notif.contains("Welcome")
                );
        }

        @Test
        @DisplayName("Should throw exception for invalid user")
        void shouldThrowExceptionForInvalidUser() {
            // Given - user with invalid email
            User invalidUser = new User("1", "invalid-email", "John Doe");

            // When & Then
            Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(invalidUser)
            );

            assertThat(exception.getMessage())
                .contains("Invalid user");

            assertEquals(0, userService.countUsers());
        }

        @Test
        @DisplayName("Should throw exception for duplicate email")
        void shouldThrowExceptionForDuplicateEmail() {
            // Given
            User user1 = new User("1", "test@example.com", "John Doe");
            User user2 = new User("2", "test@example.com", "Jane Doe");

            userService.registerUser(user1);

            // When & Then
            assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.registerUser(user2)
            );

            assertEquals(1, userService.countUsers());
        }
    }

    @Nested
    @DisplayName("User Retrieval Tests")
    class UserRetrievalTests {

        @Test
        @DisplayName("Should get user by ID")
        void shouldGetUserById() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");
            userService.registerUser(user);

            // When
            Optional<User> found = userService.getUserById("1");

            // Then
            assertTrue(found.isPresent());
            assertEquals(user.getId(), found.get().getId());
        }

        @Test
        @DisplayName("Should return empty for non-existent ID")
        void shouldReturnEmptyForNonExistentId() {
            // When
            Optional<User> found = userService.getUserById("non-existent");

            // Then
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should get user by email")
        void shouldGetUserByEmail() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");
            userService.registerUser(user);

            // When
            Optional<User> found = userService.getUserByEmail("test@example.com");

            // Then
            assertTrue(found.isPresent());
            assertEquals(user.getEmail(), found.get().getEmail());
        }

        @Test
        @DisplayName("Should get only active users")
        void shouldGetOnlyActiveUsers() {
            // Given
            User user1 = new User("1", "test1@example.com", "User 1");
            User user2 = new User("2", "test2@example.com", "User 2");
            User user3 = new User("3", "test3@example.com", "User 3");

            userService.registerUser(user1);
            userService.registerUser(user2);
            userService.registerUser(user3);

            // Deactivate one user
            userService.deactivateUser("2");

            // When
            List<User> activeUsers = userService.getActiveUsers();

            // Then
            assertThat(activeUsers)
                .hasSize(2)
                .extracting(User::getId)
                .contains("1", "3")
                .doesNotContain("2");
        }
    }

    @Nested
    @DisplayName("User Activation Tests")
    class UserActivationTests {

        @Test
        @DisplayName("Should deactivate user and send notification")
        void shouldDeactivateUserAndSendNotification() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");
            userService.registerUser(user);
            notifier.clear(); // Clear registration notification

            // When
            userService.deactivateUser("1");

            // Then
            Optional<User> found = userService.getUserById("1");
            assertTrue(found.isPresent());
            assertFalse(found.get().isActive());

            // Verify notification was sent
            assertThat(notifier.getSentNotifications())
                .hasSize(1)
                .anyMatch(notif -> notif.contains("deactivated"));
        }

        @Test
        @DisplayName("Should activate user and send notification")
        void shouldActivateUserAndSendNotification() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");
            userService.registerUser(user);
            userService.deactivateUser("1");
            notifier.clear();

            // When
            userService.activateUser("1");

            // Then
            Optional<User> found = userService.getUserById("1");
            assertTrue(found.isPresent());
            assertTrue(found.get().isActive());

            // Verify notification was sent
            assertThat(notifier.getSentNotifications())
                .hasSize(1)
                .anyMatch(notif -> notif.contains("activated"));
        }

        @Test
        @DisplayName("Should throw exception when deactivating non-existent user")
        void shouldThrowExceptionWhenDeactivatingNonExistentUser() {
            // When & Then
            assertThrows(
                UserNotFoundException.class,
                () -> userService.deactivateUser("non-existent")
            );
        }

        @Test
        @DisplayName("Should throw exception when activating non-existent user")
        void shouldThrowExceptionWhenActivatingNonExistentUser() {
            // When & Then
            assertThrows(
                UserNotFoundException.class,
                () -> userService.activateUser("non-existent")
            );
        }
    }

    @Nested
    @DisplayName("Complete User Lifecycle Tests")
    class CompleteUserLifecycleTests {

        @Test
        @DisplayName("Should handle complete user lifecycle")
        void shouldHandleCompleteUserLifecycle() {
            // 1. Register user
            User user = new User("1", "test@example.com", "John Doe");
            User registered = userService.registerUser(user);
            assertTrue(registered.isActive());

            // 2. Retrieve user
            Optional<User> found = userService.getUserById("1");
            assertTrue(found.isPresent());

            // 3. Deactivate user
            userService.deactivateUser("1");
            found = userService.getUserById("1");
            assertFalse(found.get().isActive());

            // 4. Verify not in active users list
            List<User> activeUsers = userService.getActiveUsers();
            assertThat(activeUsers).isEmpty();

            // 5. Reactivate user
            userService.activateUser("1");
            found = userService.getUserById("1");
            assertTrue(found.get().isActive());

            // 6. Verify back in active users list
            activeUsers = userService.getActiveUsers();
            assertThat(activeUsers).hasSize(1);

            // 7. Verify all notifications were sent (welcome, deactivate, activate)
            assertThat(notifier.getSentNotifications()).hasSize(3);
        }

        @Test
        @DisplayName("Should handle multiple users registration and operations")
        void shouldHandleMultipleUsersRegistrationAndOperations() {
            // Register multiple users
            for (int i = 1; i <= 5; i++) {
                User user = new User(
                    String.valueOf(i),
                    "user" + i + "@example.com",
                    "User " + i
                );
                userService.registerUser(user);
            }

            // Verify all registered
            assertEquals(5, userService.countUsers());

            // Deactivate some users
            userService.deactivateUser("2");
            userService.deactivateUser("4");

            // Verify active users count
            List<User> activeUsers = userService.getActiveUsers();
            assertThat(activeUsers).hasSize(3);

            // Verify each can be retrieved
            for (int i = 1; i <= 5; i++) {
                Optional<User> found = userService.getUserById(String.valueOf(i));
                assertTrue(found.isPresent());
            }
        }
    }

    /**
     * Test implementation of Notifier for integration testing.
     * Captures notifications for verification.
     */
    private static class TestNotifier implements Notifier {
        private final java.util.List<String> sentNotifications = new java.util.ArrayList<>();

        @Override
        public boolean sendNotification(String recipient, String message) {
            sentNotifications.add("To: " + recipient + " - " + message);
            return true;
        }

        public List<String> getSentNotifications() {
            return sentNotifications;
        }

        public void clear() {
            sentNotifications.clear();
        }
    }
}
