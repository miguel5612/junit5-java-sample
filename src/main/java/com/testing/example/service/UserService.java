package com.testing.example.service;

import com.testing.example.domain.User;
import com.testing.example.repository.UserRepository;
import com.testing.example.util.UserValidator;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de usuarios que coordina las operaciones de negocio.
 * Aplica varios principios SOLID:
 * - Single Responsibility: maneja la lógica de negocio de usuarios
 * - Dependency Inversion: depende de abstracciones (interfaces)
 * - Open/Closed: se puede extender sin modificar
 */
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final Notifier notifier;

    /**
     * Constructor con inyección de dependencias.
     * Aplica Dependency Inversion Principle: recibe interfaces, no implementaciones.
     */
    public UserService(UserRepository userRepository, UserValidator userValidator, Notifier notifier) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.notifier = notifier;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El usuario a registrar
     * @return El usuario registrado
     * @throws IllegalArgumentException si el usuario no es válido
     * @throws UserAlreadyExistsException si ya existe un usuario con ese email
     */
    public User registerUser(User user) {
        // Validar usuario
        List<String> validationErrors = userValidator.validate(user);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Invalid user: " + String.join(", ", validationErrors));
        }

        // Verificar que no exista otro usuario con el mismo email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        // Guardar usuario
        User savedUser = userRepository.save(user);

        // Enviar notificación de bienvenida
        notifier.sendNotification(
            savedUser.getEmail(),
            "Welcome " + savedUser.getName() + "! Your account has been created."
        );

        return savedUser;
    }

    /**
     * Obtiene un usuario por su ID.
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Obtiene un usuario por su email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios activos.
     */
    public List<User> getActiveUsers() {
        return userRepository.findAllActive();
    }

    /**
     * Desactiva un usuario y envía notificación.
     */
    public void deactivateUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        User user = userOpt.get();
        user.deactivate();
        userRepository.save(user);

        notifier.sendNotification(
            user.getEmail(),
            "Your account has been deactivated."
        );
    }

    /**
     * Activa un usuario y envía notificación.
     */
    public void activateUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        User user = userOpt.get();
        user.activate();
        userRepository.save(user);

        notifier.sendNotification(
            user.getEmail(),
            "Your account has been activated."
        );
    }

    /**
     * Cuenta el total de usuarios.
     */
    public long countUsers() {
        return userRepository.count();
    }
}
