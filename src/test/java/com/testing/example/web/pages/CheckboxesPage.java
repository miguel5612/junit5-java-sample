package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object para la página de checkboxes.
 */
public class CheckboxesPage extends BasePage {

    // Locators
    private final By checkboxes = By.cssSelector("input[type='checkbox']");
    private final By pageTitle = By.cssSelector("h3");

    public CheckboxesPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Obtiene el título de la página.
     */
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    /**
     * Obtiene todos los checkboxes.
     */
    public List<WebElement> getAllCheckboxes() {
        return driver.findElements(checkboxes);
    }

    /**
     * Verifica si un checkbox está seleccionado.
     */
    public boolean isCheckboxSelected(int index) {
        return getAllCheckboxes().get(index).isSelected();
    }

    /**
     * Marca un checkbox por índice.
     */
    public CheckboxesPage checkCheckbox(int index) {
        WebElement checkbox = getAllCheckboxes().get(index);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    /**
     * Desmarca un checkbox por índice.
     */
    public CheckboxesPage uncheckCheckbox(int index) {
        WebElement checkbox = getAllCheckboxes().get(index);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    /**
     * Toggle de un checkbox por índice.
     */
    public CheckboxesPage toggleCheckbox(int index) {
        getAllCheckboxes().get(index).click();
        return this;
    }

    /**
     * Cuenta el número de checkboxes.
     */
    public int getCheckboxCount() {
        return getAllCheckboxes().size();
    }
}
