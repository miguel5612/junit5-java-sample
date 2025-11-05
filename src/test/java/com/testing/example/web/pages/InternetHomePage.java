package com.testing.example.web.pages;

import com.testing.example.web.utils.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object para la página principal de "The Internet" (https://the-internet.herokuapp.com/)
 * Esta es una aplicación web diseñada específicamente para practicar automatización de tests.
 */
public class InternetHomePage extends BasePage {

    private static final String BASE_URL = "https://the-internet.herokuapp.com/";

    // Locators
    private final By heading = By.cssSelector("h1.heading");
    private final By subheading = By.cssSelector("h2");
    private final By loginLink = By.linkText("Form Authentication");
    private final By checkboxesLink = By.linkText("Checkboxes");
    private final By dropdownLink = By.linkText("Dropdown");
    private final By addRemoveElementsLink = By.linkText("Add/Remove Elements");

    public InternetHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navega a la página principal.
     */
    public InternetHomePage open() {
        navigateTo(BASE_URL);
        return this;
    }

    /**
     * Obtiene el título principal de la página.
     */
    public String getHeadingText() {
        return getText(heading);
    }

    /**
     * Obtiene el subtítulo de la página.
     */
    public String getSubheadingText() {
        return getText(subheading);
    }

    /**
     * Navega a la página de login.
     */
    public LoginPage goToLoginPage() {
        click(loginLink);
        return new LoginPage(driver);
    }

    /**
     * Navega a la página de checkboxes.
     */
    public CheckboxesPage goToCheckboxesPage() {
        click(checkboxesLink);
        return new CheckboxesPage(driver);
    }

    /**
     * Navega a la página de dropdown.
     */
    public DropdownPage goToDropdownPage() {
        click(dropdownLink);
        return new DropdownPage(driver);
    }

    /**
     * Navega a la página de Add/Remove Elements.
     */
    public AddRemoveElementsPage goToAddRemoveElementsPage() {
        click(addRemoveElementsLink);
        return new AddRemoveElementsPage(driver);
    }

    /**
     * Verifica si la página principal está cargada correctamente.
     */
    public boolean isLoaded() {
        return isElementVisible(heading) && getHeadingText().contains("Welcome to the-internet");
    }
}
