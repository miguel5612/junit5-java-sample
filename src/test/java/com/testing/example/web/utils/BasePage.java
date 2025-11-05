package com.testing.example.web.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase base para todas las Page Objects.
 * Proporciona métodos comunes de interacción con elementos web.
 * Aplica el patrón Page Object Model (POM).
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Navega a una URL específica.
     */
    protected void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Obtiene el título de la página actual.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Obtiene la URL actual.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Espera a que un elemento sea visible y lo retorna.
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Espera a que un elemento sea clickeable y lo retorna.
     */
    protected WebElement waitForClickableElement(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Hace click en un elemento.
     */
    protected void click(By locator) {
        waitForClickableElement(locator).click();
    }

    /**
     * Escribe texto en un campo.
     */
    protected void type(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Obtiene el texto de un elemento.
     */
    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }

    /**
     * Verifica si un elemento está visible.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return waitForElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Espera un tiempo específico (usar con moderación).
     */
    protected void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
