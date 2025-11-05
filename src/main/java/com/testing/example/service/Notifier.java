package com.testing.example.service;

/**
 * Interfaz para notificaciones.
 * Aplica Interface Segregation Principle: interfaz pequeña y específica.
 * Aplica Dependency Inversion Principle: las clases dependen de esta abstracción.
 */
public interface Notifier {

    /**
     * Envía una notificación a un destinatario.
     *
     * @param recipient El destinatario de la notificación
     * @param message El mensaje a enviar
     * @return true si la notificación fue enviada exitosamente
     */
    boolean sendNotification(String recipient, String message);
}
