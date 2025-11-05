package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object para la página de dropdown.
 */
public class DropdownPage extends BasePage {

    // Locators
    private final By dropdown = By.id("dropdown");
    private final By pageTitle = By.cssSelector("h3");

    public DropdownPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Obtiene el título de la página.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Selecciona una opción por texto visible.
     */
    public DropdownPage selectByVisibleText(String text) {
        Select select = new Select(waitForElement(dropdown));
        select.selectByVisibleText(text);
        return this;
    }

    /**
     * Selecciona una opción por valor.
     */
    public DropdownPage selectByValue(String value) {
        Select select = new Select(waitForElement(dropdown));
        select.selectByValue(value);
        return this;
    }

    /**
     * Selecciona una opción por índice.
     */
    public DropdownPage selectByIndex(int index) {
        Select select = new Select(waitForElement(dropdown));
        select.selectByIndex(index);
        return this;
    }

    /**
     * Obtiene el texto de la opción seleccionada.
     */
    public String getSelectedOptionText() {
        Select select = new Select(waitForElement(dropdown));
        return select.getFirstSelectedOption().getText();
    }

    /**
     * Obtiene el valor de la opción seleccionada.
     */
    public String getSelectedOptionValue() {
        Select select = new Select(waitForElement(dropdown));
        return select.getFirstSelectedOption().getAttribute("value");
    }
}
