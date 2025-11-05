package com.testing.example.repository;

import com.testing.example.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de usuarios.
 * Útil para testing y desarrollo.
 * Aplica Open/Closed Principle: se puede extender sin modificar la interfaz.
 */
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<User> findAllActive() {
        return storage.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return storage.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public long count() {
        return storage.size();
    }

    /**
     * Método útil para testing: limpia todo el repositorio.
     */
    public void clear() {
        storage.clear();
    }
}
