# JUnit 5 Java Sample - Proyecto Base de Testing

Proyecto base de testing con JUnit 5 que demuestra buenas prácticas, principios SOLID y técnicas avanzadas de testing en Java.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Principios SOLID Aplicados](#principios-solid-aplicados)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tipos de Tests](#tipos-de-tests)
- [Buenas Prácticas de Testing](#buenas-prácticas-de-testing)
- [Instalación y Uso](#instalación-y-uso)
- [Ejecución de Tests](#ejecución-de-tests)

## Descripción

Este proyecto sirve como base de referencia para implementar testing en aplicaciones Java, demostrando:

- **JUnit 5 (Jupiter)**: Framework de testing moderno con soporte para tests parametrizados, dinámicos y nested
- **Mockito**: Framework de mocking para tests unitarios
- **AssertJ**: Librería de assertions expresivas y fluidas
- **Principios SOLID**: Aplicados en el diseño de clases
- **Buenas prácticas**: Nomenclatura, organización y patrones de testing

## Tecnologías

| Tecnología | Versión | Descripción |
|-----------|---------|-------------|
| Java | 11+ | Lenguaje de programación |
| Maven | 3.6+ | Gestión de dependencias y build |
| JUnit 5 | 5.10.1 | Framework de testing |
| Mockito | 5.8.0 | Framework de mocking |
| AssertJ | 3.24.2 | Assertions expresivas |

## Principios SOLID Aplicados

### Single Responsibility Principle (SRP)
Cada clase tiene una única responsabilidad:

- `User`: Solo maneja datos del usuario
- `UserValidator`: Solo se encarga de validación
- `UserService`: Solo coordina lógica de negocio
- `EmailNotifier`: Solo maneja envío de notificaciones

### Open/Closed Principle (OCP)
Las clases están abiertas a extensión pero cerradas a modificación:

- `UserRepository` es una interfaz que permite múltiples implementaciones
- `InMemoryUserRepository` puede extenderse sin modificar el servicio

### Liskov Substitution Principle (LSP)
Las implementaciones pueden sustituirse por sus interfaces:

- Cualquier implementación de `UserRepository` funciona en `UserService`
- Cualquier implementación de `Notifier` funciona correctamente

### Interface Segregation Principle (ISP)
Interfaces pequeñas y específicas:

- `UserRepository`: Operaciones específicas de usuarios
- `Notifier`: Solo operaciones de notificación

### Dependency Inversion Principle (DIP)
Las clases dependen de abstracciones, no de implementaciones concretas:

- `UserService` depende de interfaces (`UserRepository`, `Notifier`)
- Facilita testing con mocks y diferentes implementaciones

## Estructura del Proyecto

```
src/
├── main/java/com/testing/example/
│   ├── domain/          # Entidades de dominio
│   │   └── User.java
│   ├── repository/      # Capa de persistencia
│   │   ├── UserRepository.java
│   │   └── InMemoryUserRepository.java
│   ├── service/         # Lógica de negocio
│   │   ├── UserService.java
│   │   ├── Notifier.java
│   │   ├── EmailNotifier.java
│   │   └── *Exception.java
│   └── util/            # Utilidades
│       └── UserValidator.java
└── test/java/com/testing/example/
    ├── unit/            # Tests unitarios
    │   ├── UserTest.java
    │   ├── UserValidatorTest.java
    │   ├── InMemoryUserRepositoryTest.java
    │   └── UserServiceMockTest.java
    ├── integration/     # Tests de integración
    │   └── UserServiceIntegrationTest.java
    └── parameterized/   # Tests parametrizados
        ├── ParameterizedEmailValidationTest.java
        └── DynamicTestsExample.java
```

## Tipos de Tests

### 1. Tests Unitarios

Tests que validan una única unidad de código de forma aislada.

**Ejemplo**: `UserTest.java`
```java
@Test
@DisplayName("Should create user with valid parameters")
void shouldCreateUserWithValidParameters() {
    User user = new User("1", "test@example.com", "John Doe");

    assertEquals("1", user.getId());
    assertTrue(user.isActive());
}
```

**Características**:
- Rápidos de ejecutar
- No dependen de recursos externos
- Usan `@Test` de JUnit 5
- Organizados con `@Nested` para agrupación lógica

### 2. Tests con Mocks (Mockito)

Tests unitarios que usan mocks para aislar dependencias.

**Ejemplo**: `UserServiceMockTest.java`
```java
@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {
    @Mock private UserRepository repository;
    @InjectMocks private UserService service;

    @Test
    void shouldRegisterUser() {
        when(repository.save(any())).thenReturn(user);

        User result = service.registerUser(user);

        verify(repository).save(user);
    }
}
```

**Características**:
- Usa `@Mock` para crear mocks
- Usa `@InjectMocks` para inyección automática
- `when().thenReturn()` para stubbing
- `verify()` para verificar interacciones
- `ArgumentCaptor` para capturar argumentos

### 3. Tests de Integración

Tests que validan la interacción entre múltiples componentes.

**Ejemplo**: `UserServiceIntegrationTest.java`
```java
@Test
@DisplayName("Should register valid user successfully")
void shouldRegisterValidUserSuccessfully() {
    // Usa implementaciones reales
    UserService service = new UserService(
        new InMemoryUserRepository(),
        new UserValidator(),
        new TestNotifier()
    );

    User registered = service.registerUser(user);

    assertNotNull(registered);
}
```

**Características**:
- Usa implementaciones reales (no mocks)
- Valida flujos completos end-to-end
- Puede ser más lento que tests unitarios

### 4. Tests Parametrizados

Tests que ejecutan el mismo test con múltiples conjuntos de datos.

**Ejemplo**: `ParameterizedEmailValidationTest.java`
```java
@ParameterizedTest
@ValueSource(strings = {"test@example.com", "user@domain.co.uk"})
void shouldAcceptValidEmails(String email) {
    User user = new User("1", email, "Test");
    assertTrue(validator.isValid(user));
}
```

**Fuentes de datos disponibles**:
- `@ValueSource`: Arrays simples
- `@CsvSource`: Datos CSV
- `@MethodSource`: Método que provee datos
- `@EnumSource`: Valores de enum

### 5. Tests Dinámicos

Tests generados en tiempo de ejecución con `@TestFactory`.

**Ejemplo**: `DynamicTestsExample.java`
```java
@TestFactory
Collection<DynamicTest> dynamicTests() {
    return emails.stream()
        .map(email -> dynamicTest(
            "Testing: " + email,
            () -> assertTrue(isValid(email))
        ))
        .toList();
}
```

## Buenas Prácticas de Testing

### Nomenclatura de Tests

**Patrón recomendado**: `should[ExpectedBehavior]When[StateUnderTest]`

```java
// ✅ Buenos nombres
void shouldReturnUserWhenIdExists()
void shouldThrowExceptionWhenEmailIsInvalid()
void shouldActivateUserWhenCurrentlyInactive()

// ❌ Nombres poco descriptivos
void test1()
void testUser()
void checkEmail()
```

### Estructura AAA (Arrange-Act-Assert)

```java
@Test
void shouldRegisterUser() {
    // Arrange (Given) - Preparar datos y dependencias
    User user = new User("1", "test@example.com", "John");

    // Act (When) - Ejecutar la operación
    User result = service.registerUser(user);

    // Assert (Then) - Verificar el resultado
    assertNotNull(result);
    assertEquals(user.getId(), result.getId());
}
```

### Uso de @DisplayName

Proporciona descripciones legibles en los reportes:

```java
@Test
@DisplayName("Should register user successfully when all data is valid")
void shouldRegisterUserSuccessfully() {
    // ...
}
```

### Organización con @Nested

Agrupa tests relacionados:

```java
@Nested
@DisplayName("User Registration Tests")
class UserRegistrationTests {
    @Test void shouldRegisterValidUser() { }
    @Test void shouldRejectInvalidUser() { }
}
```

### AssertJ para Assertions Expresivas

```java
// ✅ AssertJ - más expresivo
assertThat(users)
    .hasSize(3)
    .extracting(User::getName)
    .contains("John", "Jane");

// ❌ JUnit assertions básicas
assertEquals(3, users.size());
assertTrue(users.stream().anyMatch(u -> u.getName().equals("John")));
```

### Test Fixtures con @BeforeEach

```java
@BeforeEach
void setUp() {
    repository = new InMemoryUserRepository();
    validator = new UserValidator();
    service = new UserService(repository, validator, notifier);
}
```

### Limpieza con @AfterEach

```java
@AfterEach
void tearDown() {
    repository.clear();
}
```

## Instalación y Uso

### Prerrequisitos

- Java 11 o superior
- Maven 3.6 o superior

### Clonar y compilar

```bash
# Clonar el repositorio
git clone <repository-url>
cd junit5-java-sample

# Compilar el proyecto
mvn clean compile
```

## Ejecución de Tests

### Ejecutar todos los tests

```bash
mvn test
```

### Ejecutar una clase de test específica

```bash
mvn test -Dtest=UserTest
```

### Ejecutar un test específico

```bash
mvn test -Dtest=UserTest#shouldCreateUserWithValidParameters
```

### Ejecutar tests con reporte detallado

```bash
mvn test -Dsurefire.printSummary=true
```

### Ver cobertura de tests

```bash
mvn clean test
# Los reportes se generan en target/surefire-reports/
```

## Patrones de Testing Implementados

### 1. Test Doubles

- **Mocks**: Objetos simulados con expectativas (Mockito)
- **Stubs**: Implementaciones simples para testing (`TestNotifier`)
- **Fakes**: Implementaciones funcionales pero simplificadas (`InMemoryUserRepository`)

### 2. Test Data Builders

Construcción de objetos de prueba reutilizables:

```java
User validUser = new User("1", "test@example.com", "John Doe");
```

### 3. Argument Captors

Captura argumentos para verificaciones complejas:

```java
@Captor
ArgumentCaptor<User> userCaptor;

verify(repository).save(userCaptor.capture());
assertEquals("test@example.com", userCaptor.getValue().getEmail());
```

## Recursos Adicionales

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

## Contribuir

Este proyecto está diseñado como material educativo. Siéntete libre de:

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/amazing-feature`)
3. Commit tus cambios (`git commit -m 'Add some amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abrir un Pull Request

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

**Nota**: Este proyecto está diseñado con fines educativos para demostrar buenas prácticas de testing y principios SOLID en Java.
