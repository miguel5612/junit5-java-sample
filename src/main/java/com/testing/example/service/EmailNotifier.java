package com.testing.example.service;

/**
 * Implementación de notificador por email.
 * Aplica Single Responsibility Principle: solo maneja envío de emails.
 */
public class EmailNotifier implements Notifier {

    private final String smtpServer;

    public EmailNotifier(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public boolean sendNotification(String recipient, String message) {
        // Simulación de envío de email
        // En un caso real, aquí iría la lógica de envío con JavaMail o similar
        if (recipient == null || recipient.isEmpty()) {
            return false;
        }
        if (message == null || message.isEmpty()) {
            return false;
        }

        // Simular el envío
        System.out.println("Sending email to: " + recipient);
        System.out.println("Via SMTP server: " + smtpServer);
        System.out.println("Message: " + message);

        return true;
    }

    public String getSmtpServer() {
        return smtpServer;
    }
}
