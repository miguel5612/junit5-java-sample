package com.testing.example.unit;

import com.testing.example.domain.User;
import com.testing.example.repository.UserRepository;
import com.testing.example.service.*;
import com.testing.example.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserService usando Mockito.
 * Demuestra el uso de mocks, stubs, verificaciones y argument captors.
 *
 * Principios demostrados:
 * - @ExtendWith(MockitoExtension.class) para integración con JUnit 5
 * - @Mock para crear mocks
 * - @InjectMocks para inyectar mocks automáticamente
 * - when().thenReturn() para stubbing
 * - verify() para verificar interacciones
 * - ArgumentCaptor para capturar argumentos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Mock Tests")
class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private Notifier notifier;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Nested
    @DisplayName("Register User Tests with Mocks")
    class RegisterUserTestsWithMocks {

        @Test
        @DisplayName("Should register user successfully with mocked dependencies")
        void shouldRegisterUserSuccessfullyWithMockedDependencies() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            // Configurar comportamiento de los mocks
            when(userValidator.validate(user)).thenReturn(List.of()); // sin errores
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
            when(userRepository.save(user)).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            User result = userService.registerUser(user);

            // Then
            assertNotNull(result);
            assertEquals(user.getId(), result.getId());

            // Verificar que se llamaron los métodos esperados
            verify(userValidator).validate(user);
            verify(userRepository).existsByEmail(user.getEmail());
            verify(userRepository).save(user);
            verify(notifier).sendNotification(
                eq(user.getEmail()),
                contains("Welcome")
            );
        }

        @Test
        @DisplayName("Should throw exception when user validation fails")
        void shouldThrowExceptionWhenUserValidationFails() {
            // Given
            User user = new User("1", "invalid", "Test");
            List<String> errors = Arrays.asList("Email format is invalid");

            when(userValidator.validate(user)).thenReturn(errors);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));

            // Verificar que NO se llamaron otros métodos
            verify(userValidator).validate(user);
            verify(userRepository, never()).existsByEmail(anyString());
            verify(userRepository, never()).save(any(User.class));
            verify(notifier, never()).sendNotification(anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            when(userValidator.validate(user)).thenReturn(List.of());
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

            // When & Then
            assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));

            // Verificar interacciones
            verify(userValidator).validate(user);
            verify(userRepository).existsByEmail(user.getEmail());
            verify(userRepository, never()).save(any(User.class));
            verify(notifier, never()).sendNotification(anyString(), anyString());
        }

        @Test
        @DisplayName("Should capture notification message using ArgumentCaptor")
        void shouldCaptureNotificationMessageUsingArgumentCaptor() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            when(userValidator.validate(user)).thenReturn(List.of());
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
            when(userRepository.save(user)).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            userService.registerUser(user);

            // Then - capturar los argumentos pasados al notifier
            verify(notifier).sendNotification(stringCaptor.capture(), stringCaptor.capture());

            List<String> capturedArgs = stringCaptor.getAllValues();
            assertThat(capturedArgs)
                .hasSize(2)
                .satisfies(args -> {
                    assertThat(args.get(0)).isEqualTo("test@example.com"); // recipient
                    assertThat(args.get(1)).contains("Welcome", "John Doe"); // message
                });
        }
    }

    @Nested
    @DisplayName("Get User Tests with Mocks")
    class GetUserTestsWithMocks {

        @Test
        @DisplayName("Should get user by ID when exists")
        void shouldGetUserByIdWhenExists() {
            // Given
            String userId = "123";
            User user = new User(userId, "test@example.com", "John Doe");

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // When
            Optional<User> result = userService.getUserById(userId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(user, result.get());
            verify(userRepository).findById(userId);
        }

        @Test
        @DisplayName("Should return empty when user not found by ID")
        void shouldReturnEmptyWhenUserNotFoundById() {
            // Given
            String userId = "non-existent";

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When
            Optional<User> result = userService.getUserById(userId);

            // Then
            assertFalse(result.isPresent());
            verify(userRepository).findById(userId);
        }

        @Test
        @DisplayName("Should get user by email when exists")
        void shouldGetUserByEmailWhenExists() {
            // Given
            String email = "test@example.com";
            User user = new User("1", email, "John Doe");

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            // When
            Optional<User> result = userService.getUserByEmail(email);

            // Then
            assertTrue(result.isPresent());
            assertEquals(user, result.get());
            verify(userRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should get all active users")
        void shouldGetAllActiveUsers() {
            // Given
            List<User> activeUsers = Arrays.asList(
                new User("1", "user1@example.com", "User 1"),
                new User("2", "user2@example.com", "User 2")
            );

            when(userRepository.findAllActive()).thenReturn(activeUsers);

            // When
            List<User> result = userService.getActiveUsers();

            // Then
            assertThat(result)
                .hasSize(2)
                .containsExactlyElementsOf(activeUsers);
            verify(userRepository).findAllActive();
        }
    }

    @Nested
    @DisplayName("Activation/Deactivation Tests with Mocks")
    class ActivationDeactivationTestsWithMocks {

        @Test
        @DisplayName("Should deactivate user and send notification")
        void shouldDeactivateUserAndSendNotification() {
            // Given
            String userId = "1";
            User user = new User(userId, "test@example.com", "John Doe");

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            userService.deactivateUser(userId);

            // Then
            verify(userRepository).findById(userId);
            verify(userRepository).save(userCaptor.capture());
            verify(notifier).sendNotification(eq(user.getEmail()), contains("deactivated"));

            // Verificar que el usuario capturado está desactivado
            User capturedUser = userCaptor.getValue();
            assertFalse(capturedUser.isActive());
        }

        @Test
        @DisplayName("Should activate user and send notification")
        void shouldActivateUserAndSendNotification() {
            // Given
            String userId = "1";
            User user = new User(userId, "test@example.com", "John Doe");
            user.deactivate();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            userService.activateUser(userId);

            // Then
            verify(userRepository).findById(userId);
            verify(userRepository).save(userCaptor.capture());
            verify(notifier).sendNotification(eq(user.getEmail()), contains("activated"));

            // Verificar que el usuario capturado está activado
            User capturedUser = userCaptor.getValue();
            assertTrue(capturedUser.isActive());
        }

        @Test
        @DisplayName("Should throw exception when deactivating non-existent user")
        void shouldThrowExceptionWhenDeactivatingNonExistentUser() {
            // Given
            String userId = "non-existent";

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(UserNotFoundException.class, () -> userService.deactivateUser(userId));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any(User.class));
            verify(notifier, never()).sendNotification(anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw exception when activating non-existent user")
        void shouldThrowExceptionWhenActivatingNonExistentUser() {
            // Given
            String userId = "non-existent";

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(UserNotFoundException.class, () -> userService.activateUser(userId));

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any(User.class));
            verify(notifier, never()).sendNotification(anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("Count Operations with Mocks")
    class CountOperationsWithMocks {

        @Test
        @DisplayName("Should count users")
        void shouldCountUsers() {
            // Given
            when(userRepository.count()).thenReturn(5L);

            // When
            long count = userService.countUsers();

            // Then
            assertEquals(5L, count);
            verify(userRepository).count();
        }

        @Test
        @DisplayName("Should count zero users")
        void shouldCountZeroUsers() {
            // Given
            when(userRepository.count()).thenReturn(0L);

            // When
            long count = userService.countUsers();

            // Then
            assertEquals(0L, count);
            verify(userRepository).count();
        }
    }

    @Nested
    @DisplayName("Verification Patterns")
    class VerificationPatterns {

        @Test
        @DisplayName("Should verify method called exact number of times")
        void shouldVerifyMethodCalledExactNumberOfTimes() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            when(userValidator.validate(user)).thenReturn(List.of());
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            userService.registerUser(user);

            // Then - verificar llamadas exactas
            verify(userValidator, times(1)).validate(user);
            verify(userRepository, times(1)).existsByEmail(anyString());
            verify(userRepository, times(1)).save(any(User.class));
            verify(notifier, times(1)).sendNotification(anyString(), anyString());
        }

        @Test
        @DisplayName("Should verify no interactions when validation fails early")
        void shouldVerifyNoInteractionsWhenValidationFailsEarly() {
            // Given
            User user = new User("1", "invalid", "Test");
            when(userValidator.validate(user)).thenReturn(Arrays.asList("Invalid"));

            // When
            assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));

            // Then - verificar que no hubo interacciones con repository y notifier
            verifyNoInteractions(userRepository);
            verifyNoInteractions(notifier);
        }

        @Test
        @DisplayName("Should use argument matchers correctly")
        void shouldUseArgumentMatchersCorrectly() {
            // Given
            User user = new User("1", "test@example.com", "John Doe");

            when(userValidator.validate(any(User.class))).thenReturn(List.of());
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(notifier.sendNotification(anyString(), anyString())).thenReturn(true);

            // When
            userService.registerUser(user);

            // Then - usar diferentes argument matchers
            verify(userValidator).validate(any(User.class));
            verify(userRepository).existsByEmail(eq("test@example.com"));
            verify(notifier).sendNotification(
                argThat(email -> email.contains("@")),
                argThat(msg -> msg.contains("Welcome"))
            );
        }
    }
}
