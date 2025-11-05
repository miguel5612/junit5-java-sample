package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object para la página segura (después del login exitoso).
 */
public class SecureAreaPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("h2");
    private final By flashMessage = By.id("flash");
    private final By logoutButton = By.cssSelector("a[href='/logout']");
    private final By secureAreaText = By.id("content");

    public SecureAreaPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Obtiene el título de la página.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Obtiene el mensaje flash.
     */
    public String getFlashMessage() {
        return getText(flashMessage);
    }

    /**
     * Verifica si el login fue exitoso.
     */
    public boolean isLoginSuccessful() {
        return getFlashMessage().contains("You logged into a secure area!");
    }

    /**
     * Hace click en el botón de logout.
     */
    public LoginPage logout() {
        click(logoutButton);
        return new LoginPage(driver);
    }

    /**
     * Verifica si el botón de logout está visible.
     */
    public boolean isLogoutButtonVisible() {
        return isElementVisible(logoutButton);
    }

    /**
     * Obtiene el texto del área segura.
     */
    public String getSecureAreaText() {
        return getText(secureAreaText);
    }
}
