package com.testing.example.web;

import com.testing.example.web.pages.*;
import com.testing.example.web.utils.TestReportGenerator;
import com.testing.example.web.utils.WebDriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de automatización web usando Selenium WebDriver.
 * Demuestra el patrón Page Object Model (POM) y buenas prácticas de testing web.
 *
 * Los tests se ejecutan contra "The Internet" (https://the-internet.herokuapp.com/)
 * una aplicación diseñada específicamente para practicar automatización de tests.
 */
@DisplayName("Web Automation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebAutomationTest {

    private static WebDriver driver;
    private static TestReportGenerator reportGenerator;
    private InternetHomePage homePage;

    @BeforeAll
    static void setUpClass() {
        reportGenerator = new TestReportGenerator("Web Automation Test Report");
        reportGenerator.startReport();
    }

    @BeforeEach
    void setUp() {
        // Usar Chrome en modo headless para CI/CD
        driver = WebDriverFactory.createDefaultDriver();
        homePage = new InternetHomePage(driver);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if (driver != null) {
            boolean testPassed = testInfo.getTags().contains("failed") == false;
            reportGenerator.addTestResult(
                testInfo.getDisplayName(),
                testPassed ? "PASSED" : "FAILED",
                driver.getCurrentUrl()
            );
            driver.quit();
        }
    }

    @AfterAll
    static void tearDownClass() {
        reportGenerator.endReport();
        reportGenerator.saveReport("target/test-reports/web-automation-report.html");
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 REPORTE DE TESTS WEB GENERADO");
        System.out.println("=".repeat(80));
        System.out.println("📄 Ubicación: target/test-reports/web-automation-report.html");
        System.out.println("=".repeat(80) + "\n");
    }

    @Test
    @Order(1)
    @DisplayName("01. Verificar que la página principal se carga correctamente")
    void shouldLoadHomePage() {
        // When
        homePage.open();

        // Then
        assertTrue(homePage.isLoaded(), "La página principal debería cargarse correctamente");
        assertThat(homePage.getHeadingText())
            .contains("Welcome to the-internet");
        assertThat(homePage.getPageTitle())
            .isEqualTo("The Internet");

        System.out.println("✓ Página principal cargada: " + driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    @DisplayName("02. Login exitoso con credenciales válidas")
    void shouldLoginSuccessfully() {
        // Given
        homePage.open();
        LoginPage loginPage = homePage.goToLoginPage();

        // When
        SecureAreaPage secureArea = loginPage.login("tomsmith", "SuperSecretPassword!");

        // Then
        assertTrue(secureArea.isLoginSuccessful(), "El login debería ser exitoso");
        assertThat(secureArea.getPageTitleText()).contains("Secure Area");
        assertTrue(secureArea.isLogoutButtonVisible(), "El botón de logout debería estar visible");

        System.out.println("✓ Login exitoso realizado");
        System.out.println("  - Usuario: tomsmith");
        System.out.println("  - Mensaje: " + secureArea.getFlashMessage().trim());
    }

    @Test
    @Order(3)
    @DisplayName("03. Login fallido con credenciales inválidas")
    void shouldFailLoginWithInvalidCredentials() {
        // Given
        homePage.open();
        LoginPage loginPage = homePage.goToLoginPage();

        // When
        loginPage.loginExpectingFailure("invalidUser", "invalidPassword");

        // Then
        assertTrue(loginPage.flashMessageContains("Your username is invalid!"),
            "Debería mostrar mensaje de error");

        System.out.println("✓ Login fallido detectado correctamente");
        System.out.println("  - Mensaje de error: " + loginPage.getFlashMessage().trim());
    }

    @Test
    @Order(4)
    @DisplayName("04. Login y Logout completo")
    void shouldLoginAndLogout() {
        // Given
        homePage.open();
        LoginPage loginPage = homePage.goToLoginPage();

        // When - Login
        SecureAreaPage secureArea = loginPage.login("tomsmith", "SuperSecretPassword!");
        assertTrue(secureArea.isLoginSuccessful());

        // When - Logout
        LoginPage logoutPage = secureArea.logout();

        // Then
        assertTrue(logoutPage.flashMessageContains("You logged out"),
            "Debería mostrar mensaje de logout exitoso");

        System.out.println("✓ Ciclo completo Login/Logout realizado");
    }

    @Test
    @Order(5)
    @DisplayName("05. Interacción con checkboxes")
    void shouldInteractWithCheckboxes() {
        // Given
        homePage.open();
        CheckboxesPage checkboxesPage = homePage.goToCheckboxesPage();

        // When & Then
        assertThat(checkboxesPage.getCheckboxCount()).isEqualTo(2);

        // Verificar estado inicial
        boolean checkbox1InitialState = checkboxesPage.isCheckboxSelected(0);
        boolean checkbox2InitialState = checkboxesPage.isCheckboxSelected(1);

        System.out.println("✓ Estados iniciales de checkboxes:");
        System.out.println("  - Checkbox 1: " + (checkbox1InitialState ? "marcado" : "desmarcado"));
        System.out.println("  - Checkbox 2: " + (checkbox2InitialState ? "marcado" : "desmarcado"));

        // Marcar el primer checkbox
        checkboxesPage.checkCheckbox(0);
        assertTrue(checkboxesPage.isCheckboxSelected(0), "Checkbox 1 debería estar marcado");

        // Desmarcar el segundo checkbox
        checkboxesPage.uncheckCheckbox(1);
        assertFalse(checkboxesPage.isCheckboxSelected(1), "Checkbox 2 debería estar desmarcado");

        System.out.println("✓ Checkboxes manipulados correctamente");
    }

    @Test
    @Order(6)
    @DisplayName("06. Selección de opciones en dropdown")
    void shouldSelectDropdownOptions() {
        // Given
        homePage.open();
        DropdownPage dropdownPage = homePage.goToDropdownPage();

        // When - Seleccionar "Option 1"
        dropdownPage.selectByVisibleText("Option 1");

        // Then
        assertThat(dropdownPage.getSelectedOptionText()).isEqualTo("Option 1");
        assertThat(dropdownPage.getSelectedOptionValue()).isEqualTo("1");

        System.out.println("✓ Opción 1 seleccionada: " + dropdownPage.getSelectedOptionText());

        // When - Seleccionar "Option 2"
        dropdownPage.selectByValue("2");

        // Then
        assertThat(dropdownPage.getSelectedOptionText()).isEqualTo("Option 2");
        assertThat(dropdownPage.getSelectedOptionValue()).isEqualTo("2");

        System.out.println("✓ Opción 2 seleccionada: " + dropdownPage.getSelectedOptionText());
    }

    @Test
    @Order(7)
    @DisplayName("07. Agregar y remover elementos dinámicamente")
    void shouldAddAndRemoveElements() {
        // Given
        homePage.open();
        AddRemoveElementsPage page = homePage.goToAddRemoveElementsPage();

        // When - Agregar elementos
        page.addMultipleElements(5);

        // Then
        assertThat(page.getAddedElementsCount()).isEqualTo(5);
        assertTrue(page.hasAddedElements(), "Deberían existir elementos agregados");

        System.out.println("✓ Elementos agregados: " + page.getAddedElementsCount());

        // When - Remover un elemento
        page.deleteFirstElement();

        // Then
        assertThat(page.getAddedElementsCount()).isEqualTo(4);

        System.out.println("✓ Elemento removido. Elementos restantes: " + page.getAddedElementsCount());

        // When - Remover todos los elementos
        page.deleteAllElements();

        // Then
        assertThat(page.getAddedElementsCount()).isEqualTo(0);
        assertFalse(page.hasAddedElements(), "No deberían existir elementos");

        System.out.println("✓ Todos los elementos removidos");
    }

    @Test
    @Order(8)
    @DisplayName("08. Flujo completo de navegación entre páginas")
    void shouldNavigateThroughMultiplePages() {
        // Given
        homePage.open();
        assertTrue(homePage.isLoaded());
        System.out.println("✓ Paso 1: Página principal cargada");

        // When - Navegar a checkboxes
        CheckboxesPage checkboxesPage = homePage.goToCheckboxesPage();
        assertThat(checkboxesPage.getPageTitleText()).isEqualTo("Checkboxes");
        System.out.println("✓ Paso 2: Navegado a página de Checkboxes");

        // When - Volver al home y navegar a dropdown
        driver.navigate().back();
        DropdownPage dropdownPage = homePage.goToDropdownPage();
        assertThat(dropdownPage.getPageTitleText()).isEqualTo("Dropdown List");
        System.out.println("✓ Paso 3: Navegado a página de Dropdown");

        // When - Volver al home y navegar a login
        driver.navigate().back();
        LoginPage loginPage = homePage.goToLoginPage();
        assertThat(loginPage.getPageTitleText()).contains("Login Page");
        System.out.println("✓ Paso 4: Navegado a página de Login");

        System.out.println("✓ Flujo de navegación completado exitosamente");
    }

    @Nested
    @DisplayName("Tests de validación de elementos UI")
    class UIValidationTests {

        @Test
        @DisplayName("Validar que todos los títulos de página son correctos")
        void shouldValidatePageTitles() {
            // Home Page
            homePage.open();
            assertThat(homePage.getHeadingText()).contains("Welcome to the-internet");

            // Login Page
            LoginPage loginPage = homePage.goToLoginPage();
            assertThat(loginPage.getPageTitleText()).contains("Login Page");
            driver.navigate().back();

            // Checkboxes Page
            CheckboxesPage checkboxesPage = homePage.goToCheckboxesPage();
            assertThat(checkboxesPage.getPageTitleText()).isEqualTo("Checkboxes");
            driver.navigate().back();

            // Dropdown Page
            DropdownPage dropdownPage = homePage.goToDropdownPage();
            assertThat(dropdownPage.getPageTitleText()).isEqualTo("Dropdown List");

            System.out.println("✓ Todos los títulos de página validados correctamente");
        }
    }
}
