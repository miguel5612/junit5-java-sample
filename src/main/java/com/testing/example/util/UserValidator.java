package com.testing.example.util;

import com.testing.example.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador de usuarios.
 * Aplica Single Responsibility Principle: solo se encarga de validar usuarios.
 */
public class UserValidator {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;

    /**
     * Valida un usuario y retorna una lista de errores encontrados.
     * Si la lista está vacía, el usuario es válido.
     */
    public List<String> validate(User user) {
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add("User cannot be null");
            return errors;
        }

        validateId(user.getId(), errors);
        validateEmail(user.getEmail(), errors);
        validateName(user.getName(), errors);

        return errors;
    }

    /**
     * Valida si un usuario es válido (sin errores).
     */
    public boolean isValid(User user) {
        return validate(user).isEmpty();
    }

    private void validateId(String id, List<String> errors) {
        if (id == null || id.trim().isEmpty()) {
            errors.add("User ID cannot be null or empty");
        }
    }

    private void validateEmail(String email, List<String> errors) {
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email cannot be null or empty");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email format is invalid");
        }
    }

    private void validateName(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add("Name cannot be null or empty");
        } else if (name.length() < MIN_NAME_LENGTH) {
            errors.add("Name must be at least " + MIN_NAME_LENGTH + " characters");
        } else if (name.length() > MAX_NAME_LENGTH) {
            errors.add("Name cannot exceed " + MAX_NAME_LENGTH + " characters");
        }
    }
}
