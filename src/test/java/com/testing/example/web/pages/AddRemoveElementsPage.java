package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para la página de Add/Remove Elements.
 */
public class AddRemoveElementsPage extends BasePage {

    // Locators
    private final By addButton = By.cssSelector("button[onclick='addElement()']");
    private final By deleteButtons = By.className("added-manually");
    private final By pageTitle = By.cssSelector("h3");

    public AddRemoveElementsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Obtiene el título de la página.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Hace click en el botón "Add Element".
     */
    public AddRemoveElementsPage clickAddElement() {
        click(addButton);
        return this;
    }

    /**
     * Agrega múltiples elementos.
     */
    public AddRemoveElementsPage addMultipleElements(int count) {
        for (int i = 0; i < count; i++) {
            clickAddElement();
        }
        return this;
    }

    /**
     * Obtiene la lista de botones "Delete".
     */
    public List<WebElement> getDeleteButtons() {
        try {
            return driver.findElements(deleteButtons);
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Cuenta el número de elementos agregados.
     */
    public int getAddedElementsCount() {
        return getDeleteButtons().size();
    }

    /**
     * Elimina el primer elemento.
     */
    public AddRemoveElementsPage deleteFirstElement() {
        List<WebElement> buttons = getDeleteButtons();
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
        return this;
    }

    /**
     * Elimina todos los elementos.
     */
    public AddRemoveElementsPage deleteAllElements() {
        List<WebElement> buttons = getDeleteButtons();
        while (!buttons.isEmpty()) {
            buttons.get(0).click();
            buttons = getDeleteButtons();
        }
        return this;
    }

    /**
     * Verifica si hay elementos agregados.
     */
    public boolean hasAddedElements() {
        return getAddedElementsCount() > 0;
    }
}
