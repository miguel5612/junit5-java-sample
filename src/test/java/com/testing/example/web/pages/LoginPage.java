package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object para la página de login.
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By flashMessage = By.id("flash");
    private final By pageTitle = By.cssSelector("h2");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Obtiene el título de la página.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Ingresa el nombre de usuario.
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    /**
     * Ingresa la contraseña.
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    /**
     * Hace click en el botón de login.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Realiza el proceso completo de login.
     */
    public SecureAreaPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new SecureAreaPage(driver);
    }

    /**
     * Intenta hacer login esperando un error.
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    /**
     * Obtiene el mensaje flash (éxito o error).
     */
    public String getFlashMessage() {
        return getText(flashMessage);
    }

    /**
     * Verifica si el mensaje flash contiene un texto específico.
     */
    public boolean flashMessageContains(String text) {
        return getFlashMessage().contains(text);
    }
}
