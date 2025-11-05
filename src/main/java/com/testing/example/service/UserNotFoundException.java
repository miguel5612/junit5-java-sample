package com.testing.example.service;

/**
 * Excepción lanzada cuando no se encuentra un usuario.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
