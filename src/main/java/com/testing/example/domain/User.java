package com.testing.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase de dominio User que representa un usuario del sistema.
 * Aplica Single Responsibility Principle: solo maneja los datos del usuario.
 */
public class User {
    private final String id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;
    private boolean active;

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    // Constructor para testing con fecha específica
    public User(String id, String email, String name, LocalDateTime createdAt, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                '}';
    }
}
