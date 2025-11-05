package com.testing.example.unit;

import com.testing.example.domain.User;
import com.testing.example.repository.InMemoryUserRepository;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InMemoryUserRepository.
 * Demuestra el uso de @BeforeEach, @AfterEach y testing de repositorios.
 */
@DisplayName("InMemoryUserRepository Tests")
class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Nested
    @DisplayName("Save Operations")
    class SaveOperations {

        @Test
        @DisplayName("Should save user successfully")
        void shouldSaveUserSuccessfully() {
            // Given
            User user = new User("1", "test@example.com", "Test User");

            // When
            User saved = repository.save(user);

            // Then
            assertNotNull(saved);
            assertEquals(user, saved);
            assertEquals(1, repository.count());
        }

        @Test
        @DisplayName("Should throw exception when saving null user")
        void shouldThrowExceptionWhenSavingNullUser() {
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        }

        @Test
        @DisplayName("Should update existing user")
        void shouldUpdateExistingUser() {
            // Given
            User user = new User("1", "test@example.com", "Test User");
            repository.save(user);

            // When
            user.deactivate();
            User updated = repository.save(user);

            // Then
            assertFalse(updated.isActive());
            assertEquals(1, repository.count());
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindOperations {

        @Test
        @DisplayName("Should find user by ID")
        void shouldFindUserById() {
            // Given
            User user = new User("1", "test@example.com", "Test User");
            repository.save(user);

            // When
            Optional<User> found = repository.findById("1");

            // Then
            assertTrue(found.isPresent());
            assertEquals(user, found.get());
        }

        @Test
        @DisplayName("Should return empty when user not found by ID")
        void shouldReturnEmptyWhenUserNotFoundById() {
            // When
            Optional<User> found = repository.findById("non-existent");

            // Then
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should find user by email")
        void shouldFindUserByEmail() {
            // Given
            User user = new User("1", "test@example.com", "Test User");
            repository.save(user);

            // When
            Optional<User> found = repository.findByEmail("test@example.com");

            // Then
            assertTrue(found.isPresent());
            assertEquals(user, found.get());
        }

        @Test
        @DisplayName("Should return empty when user not found by email")
        void shouldReturnEmptyWhenUserNotFoundByEmail() {
            // When
            Optional<User> found = repository.findByEmail("nonexistent@example.com");

            // Then
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should find all users")
        void shouldFindAllUsers() {
            // Given
            User user1 = new User("1", "test1@example.com", "User 1");
            User user2 = new User("2", "test2@example.com", "User 2");
            User user3 = new User("3", "test3@example.com", "User 3");
            repository.save(user1);
            repository.save(user2);
            repository.save(user3);

            // When
            List<User> users = repository.findAll();

            // Then
            assertThat(users)
                .hasSize(3)
                .contains(user1, user2, user3);
        }

        @Test
        @DisplayName("Should find only active users")
        void shouldFindOnlyActiveUsers() {
            // Given
            User activeUser1 = new User("1", "test1@example.com", "User 1");
            User activeUser2 = new User("2", "test2@example.com", "User 2");
            User inactiveUser = new User("3", "test3@example.com", "User 3");
            inactiveUser.deactivate();

            repository.save(activeUser1);
            repository.save(activeUser2);
            repository.save(inactiveUser);

            // When
            List<User> activeUsers = repository.findAllActive();

            // Then
            assertThat(activeUsers)
                .hasSize(2)
                .contains(activeUser1, activeUser2)
                .doesNotContain(inactiveUser);
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        @DisplayName("Should delete user by ID")
        void shouldDeleteUserById() {
            // Given
            User user = new User("1", "test@example.com", "Test User");
            repository.save(user);

            // When
            boolean deleted = repository.deleteById("1");

            // Then
            assertTrue(deleted);
            assertEquals(0, repository.count());
            assertFalse(repository.findById("1").isPresent());
        }

        @Test
        @DisplayName("Should return false when deleting non-existent user")
        void shouldReturnFalseWhenDeletingNonExistentUser() {
            // When
            boolean deleted = repository.deleteById("non-existent");

            // Then
            assertFalse(deleted);
        }
    }

    @Nested
    @DisplayName("Existence Check Operations")
    class ExistenceCheckOperations {

        @Test
        @DisplayName("Should return true when user exists by email")
        void shouldReturnTrueWhenUserExistsByEmail() {
            // Given
            User user = new User("1", "test@example.com", "Test User");
            repository.save(user);

            // When
            boolean exists = repository.existsByEmail("test@example.com");

            // Then
            assertTrue(exists);
        }

        @Test
        @DisplayName("Should return false when user does not exist by email")
        void shouldReturnFalseWhenUserDoesNotExistByEmail() {
            // When
            boolean exists = repository.existsByEmail("nonexistent@example.com");

            // Then
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("Count Operations")
    class CountOperations {

        @Test
        @DisplayName("Should count zero users initially")
        void shouldCountZeroUsersInitially() {
            // When
            long count = repository.count();

            // Then
            assertEquals(0, count);
        }

        @Test
        @DisplayName("Should count users correctly")
        void shouldCountUsersCorrectly() {
            // Given
            repository.save(new User("1", "test1@example.com", "User 1"));
            repository.save(new User("2", "test2@example.com", "User 2"));
            repository.save(new User("3", "test3@example.com", "User 3"));

            // When
            long count = repository.count();

            // Then
            assertEquals(3, count);
        }
    }

    @Nested
    @DisplayName("Clear Operations")
    class ClearOperations {

        @Test
        @DisplayName("Should clear all users")
        void shouldClearAllUsers() {
            // Given
            repository.save(new User("1", "test1@example.com", "User 1"));
            repository.save(new User("2", "test2@example.com", "User 2"));

            // When
            repository.clear();

            // Then
            assertEquals(0, repository.count());
            assertThat(repository.findAll()).isEmpty();
        }
    }
}
