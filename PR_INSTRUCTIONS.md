# 📝 Instrucciones para Crear el Pull Request

## 🎯 Objetivo
Agregar el proyecto `junit5-java-sample` al catálogo de frameworks de automatización en:
**https://github.com/Test-Architect/automation-frameworks-catalog**

---

## 🚀 Pasos para Crear el PR

### 1️⃣ Hacer Fork del Repositorio

1. Ve a https://github.com/Test-Architect/automation-frameworks-catalog
2. Haz click en el botón **"Fork"** (esquina superior derecha)
3. Esto creará una copia del repositorio en tu cuenta de GitHub

### 2️⃣ Clonar TU Fork

```bash
# Reemplaza TU_USUARIO con tu nombre de usuario de GitHub
git clone https://github.com/TU_USUARIO/automation-frameworks-catalog.git
cd automation-frameworks-catalog
```

### 3️⃣ Crear una Nueva Rama

```bash
git checkout -b add-junit5-java-sample
```

### 4️⃣ Editar el Archivo README.md

Busca la línea 8 que dice:

```markdown
| ☕️ **Java**       | JUnit 5                     | `junit5-java-sample`           | ⏳ Pending   |                                                                         |                                                                                                                                                           |
```

Y reemplázala con:

```markdown
| ☕️ **Java**       | JUnit 5                     | `junit5-java-sample`           | ✅ Completed | [🔗](https://github.com/miguel5612/junit5-java-sample)                  | [<img src="https://github.com/miguel5612.png?size=20" alt="miguel5612" width="20" height="20" style="border-radius:50%;">](https://github.com/miguel5612) |
```

### 5️⃣ Guardar los Cambios

```bash
git add readme.md
git commit -m "Add junit5-java-sample to Java frameworks catalog"
```

### 6️⃣ Push a TU Fork

```bash
# Reemplaza TU_USUARIO con tu nombre de usuario
git push origin add-junit5-java-sample
```

### 7️⃣ Crear el Pull Request en GitHub

1. Ve a TU fork en GitHub: `https://github.com/TU_USUARIO/automation-frameworks-catalog`
2. Verás un mensaje diciendo "Compare & pull request" - haz click ahí
3. Asegúrate de que:
   - **Base repository**: `Test-Architect/automation-frameworks-catalog`
   - **Base**: `main`
   - **Head repository**: `TU_USUARIO/automation-frameworks-catalog`
   - **Compare**: `add-junit5-java-sample`

### 8️⃣ Completar el Formulario del PR

**Título:**
```
Add junit5-java-sample to Java frameworks catalog
```

**Descripción:**
```markdown
## 📦 New Framework Addition

Adding a comprehensive **JUnit 5** testing framework sample to the catalog.

### ✨ Features

This repository demonstrates a complete testing solution with:

#### 🧪 Testing Capabilities
- **Unit Tests**: Using JUnit 5 with @Nested, @DisplayName
- **Integration Tests**: Full end-to-end flows with real implementations
- **Parameterized Tests**: @ValueSource, @CsvSource, @MethodSource
- **Dynamic Tests**: @TestFactory for runtime test generation
- **Mock Testing**: Mockito with @Mock, @InjectMocks, ArgumentCaptor
- **Web Automation**: Selenium WebDriver with Page Object Model (POM)

#### 🏗️ Architecture & Design
- **SOLID Principles** applied throughout all classes
- **Page Object Model** for web automation
- **Factory Pattern** for WebDriver creation
- **Dependency Injection** for testability
- **Layered Architecture**: Domain, Service, Repository, Utils

#### 🛠️ Technologies
- Java 11
- Maven
- JUnit 5.10.1
- Selenium WebDriver 4.16.1
- Mockito 5.8.0
- AssertJ 3.24.2
- WebDriverManager 5.6.2

#### 📊 Test Coverage
- **26 Java files** total
- **17 test classes** covering:
  - Domain models (User)
  - Validators (UserValidator)
  - Repositories (InMemoryUserRepository)
  - Services (UserService)
  - Web automation (Login, Forms, Dropdowns, Dynamic Elements)

#### 🎯 Web Automation
- Automated tests against https://the-internet.herokuapp.com/
- Login/logout flows
- Form interactions (checkboxes, dropdowns)
- Dynamic element manipulation
- HTML report generation with visual statistics

#### 📚 Documentation
- Comprehensive README with:
  - Explanation of SOLID principles applied
  - Examples of each test type
  - Best practices guide
  - Execution instructions
  - Resources and references

### 🔗 Repository
https://github.com/miguel5612/junit5-java-sample

### ✅ Compliance with Guidelines

This framework follows the automation_framework_guidelines.md requirements:

- ✅ Modular, layered architecture
- ✅ Configuration-driven (pom.xml)
- ✅ Data-driven tests (parameterized)
- ✅ Reusable components (Page Objects, utilities)
- ✅ Readable naming conventions
- ✅ Built-in reporting (HTML reports)
- ✅ Parallel execution capable
- ✅ Complete documentation
- ✅ Executable sample tests included

### 👤 Contributor
@miguel5612
```

### 9️⃣ Enviar el Pull Request

Haz click en **"Create Pull Request"**

---

## 📋 Checklist Final

Antes de enviar el PR, verifica:

- [ ] La línea 8 del README está actualizada correctamente
- [ ] El enlace apunta a `https://github.com/miguel5612/junit5-java-sample`
- [ ] El estado cambió de "⏳ Pending" a "✅ Completed"
- [ ] Tu nombre de usuario aparece en la columna de Contributor
- [ ] El formato de la tabla está correcto (espacios alineados)
- [ ] El título del PR es claro
- [ ] La descripción incluye todas las características del proyecto

---

## 🎉 ¡Listo!

Una vez creado el PR:
- Los mantenedores revisarán tu contribución
- Pueden solicitar cambios o hacer preguntas
- Cuando sea aprobado, tu proyecto aparecerá en el catálogo oficial

## 📞 Soporte

Si tienes problemas, puedes:
- Revisar otros PRs cerrados para ver ejemplos: https://github.com/Test-Architect/automation-frameworks-catalog/pulls?q=is%3Apr+is%3Aclosed
- Contactar a los mantenedores (@lamhotsiagian)
