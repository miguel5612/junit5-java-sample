package com.testing.example.repository;

import com.testing.example.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio de usuarios.
 * Aplica Dependency Inversion Principle: las clases dependen de abstracciones, no de implementaciones concretas.
 * Aplica Interface Segregation Principle: interfaz específica para operaciones de usuario.
 */
public interface UserRepository {

    /**
     * Guarda un usuario en el repositorio.
     */
    User save(User user);

    /**
     * Busca un usuario por su ID.
     */
    Optional<User> findById(String id);

    /**
     * Busca un usuario por su email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Obtiene todos los usuarios.
     */
    List<User> findAll();

    /**
     * Obtiene solo los usuarios activos.
     */
    List<User> findAllActive();

    /**
     * Elimina un usuario por su ID.
     */
    boolean deleteById(String id);

    /**
     * Verifica si existe un usuario con el email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Cuenta el total de usuarios.
     */
    long count();
}
