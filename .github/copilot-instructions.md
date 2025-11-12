# Copilot Instructions for Grid Comparator

## Project Overview

This is a Java-based test automation framework using:
- **Java 21** - Programming language
- **Gradle** - Build tool (with wrapper included)
- **Serenity BDD 4.0.1** - Test reporting and framework
- **Cucumber 7.15.0** - BDD feature files and step definitions
- **Selenium WebDriver 4.16.1** - Browser automation
- **Selenoid** - Dockerized browser grid for remote execution
- **WebDriverManager 5.6.2** - Automatic driver management
- **JUnit Platform** - Test execution engine
- **AssertJ** - Fluent assertions

## Project Structure

```
src/test/java/com/jellisisland/test/automation/template/
├── pages/              # Page Object classes
├── steps/              # Serenity step libraries (reusable actions)
├── stepdefinitions/    # Cucumber step definitions (glue code)
└── runners/            # Cucumber test runners
src/test/resources/
├── features/           # Cucumber feature files (.feature)
└── serenity.properties # Serenity configuration
```

## Coding Standards & Patterns

### 1. Page Object Model (POM)
- All page objects extend `PageObject` from Serenity BDD
- Use `@DefaultUrl` annotation for page URLs
- Use `@FindBy` annotations for web elements
- Keep page objects focused on element interactions only
- Example location: `src/test/java/.../pages/GoogleHomePage.java`

```java
@DefaultUrl("https://www.google.com")
public class GoogleHomePage extends PageObject {
    @FindBy(name = "q")
    private WebElementFacade searchBox;
    
    public void enterSearchTerm(String searchTerm) {
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }
}
```

### 2. Step Libraries (Serenity Steps)
- Annotate with `@Steps` for dependency injection
- Contains business logic and reusable actions
- Call page object methods
- Example location: `src/test/java/.../steps/GoogleSearchSteps.java`

### 3. Step Definitions (Cucumber Glue)
- Map Cucumber steps to Java methods
- Use `@Given`, `@When`, `@Then` annotations
- Inject step libraries using `@Steps` annotation
- Keep thin - delegate to step libraries
- Example location: `src/test/java/.../stepdefinitions/GoogleSearchStepDefinitions.java`

```java
public class GoogleSearchStepDefinitions {
    @Steps
    GoogleSearchSteps googleSearchSteps;
    
    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        googleSearchSteps.openGoogleHomepage();
    }
}
```

### 4. Feature Files
- Use Gherkin syntax (Given/When/Then)
- Location: `src/test/resources/features/`
- Use descriptive scenario names
- Parameterize with strings using double quotes

```gherkin
Feature: Google Search
  Scenario: Basic Google search
    Given I am on the Google homepage
    When I search for "test automation"
    Then I should see search results containing "test automation"
```

### 5. Test Runners
- Extend `CucumberTestRunner` or use `@Suite` with `@ConfigurationParameter`
- Use `@CucumberOptions` to configure feature paths and glue packages
- Example location: `src/test/java/.../runners/CucumberTestRunner.java`

## Naming Conventions

- **Packages**: All lowercase, dot-separated (e.g., `com.jellisisland.test.automation.template.pages`)
- **Classes**: PascalCase (e.g., `GoogleHomePage`, `GoogleSearchSteps`)
- **Methods**: camelCase (e.g., `enterSearchTerm`, `clickSearchButton`)
- **Feature files**: snake_case with `.feature` extension (e.g., `google_search.feature`)
- **Test methods**: Use underscores for readability (e.g., `i_am_on_the_google_homepage`)

## Dependencies & Imports

When adding new test functionality:
- Check if dependencies exist in `build.gradle` before adding new ones
- Prefer Serenity BDD methods over raw Selenium when available
- Use AssertJ for assertions: `assertThat(actual).isEqualTo(expected)`
- Import from `net.serenitybdd.*` for Serenity features
- Import from `io.cucumber.*` for Cucumber features
- Import from `org.openqa.selenium.*` for Selenium features

## Configuration

### Serenity Properties (`serenity.properties`)
- Configure WebDriver (remote vs local)
- Set timeouts and waits
- Configure reporting options
- Current setup uses Selenoid remote execution on `http://localhost:4444/wd/hub`

### Environment Setup
- Selenoid runs in Docker (start with `docker-compose up -d`)
- Chrome browser with VNC enabled, video disabled
- Selenoid UI available at `http://localhost:8080`

## Building & Running Tests

### Commands to use:
```bash
# Run all tests
gradlew clean test

# Run specific feature
gradlew test -Dcucumber.features=src/test/resources/features/google_search.feature

# Generate Serenity reports
gradlew test aggregate

# Start Selenoid
docker-compose up -d

# Stop Selenoid
docker-compose down
```

### Shell Command Guidelines

**IMPORTANT**: This project uses Git Bash (MINGW64) on Windows. Follow these rules when generating shell commands:

- ✅ **Use POSIX/Unix commands**: `mkdir -p`, `rm -f`, `touch`, etc.
- ✅ **For null device**: Use `/dev/null` (not `nul` or `NUL`)
- ✅ **Create directories**: Use `mkdir -p .github` (creates if doesn't exist, no error if exists)
- ❌ **Never use Windows CMD syntax**: Avoid `2>nul`, `NUL`, `>NUL`, etc.
- ❌ **Never use workarounds**: If a command might create unwanted files (like `nul`), use the correct command instead
- ❌ **Don't add files to .gitignore to hide mistakes**: Fix the root cause, don't mask it

**Examples**:
```bash
# ✅ Correct - Create directory idempotently
mkdir -p .github

# ❌ Wrong - Creates 'nul' file in Git Bash
mkdir .github 2>nul

# ✅ Correct - Redirect to null device
command 2>/dev/null

# ❌ Wrong - Creates 'nul' file in Git Bash
command 2>nul
```

## Best Practices for AI Code Generation

1. **Always follow the three-layer pattern**: Feature File → Step Definitions → Step Libraries → Page Objects
2. **Don't bypass layers**: Step definitions should call step libraries, not page objects directly
3. **Keep concerns separated**: 
   - Page Objects = Element location + basic interactions
   - Step Libraries = Business logic + assertions
   - Step Definitions = Cucumber glue code (thin layer)
4. **Use Serenity annotations**: `@Steps`, `@DefaultUrl`, `@FindBy`
5. **Prefer explicit waits**: Use `waitFor()` methods from Serenity PageObject
6. **Write descriptive Gherkin**: Make scenarios readable for non-technical stakeholders
7. **Use AssertJ assertions**: More readable than JUnit assertions
8. **Follow Java 21 conventions**: Use modern Java features when appropriate
9. **Add JavaDoc**: Document complex methods and business logic
10. **Handle exceptions gracefully**: Provide meaningful error messages
11. **Use correct shell commands**: Git Bash requires POSIX/Unix commands, not Windows CMD syntax (see Shell Command Guidelines)

## Common Patterns

### Opening a page:
```java
homePage.open(); // Uses @DefaultUrl
```

### Waiting for elements:
```java
waitFor(element); // From Serenity PageObject
```

### Assertions in Step Libraries:
```java
import static org.assertj.core.api.Assertions.assertThat;

assertThat(resultsPage.getTitle()).contains("test automation");
```

### Parameterized Cucumber steps:
```gherkin
When I search for "selenium"
```
```java
@When("I search for {string}")
public void i_search_for(String searchTerm) {
    googleSearchSteps.searchFor(searchTerm);
}
```

## File Creation Guidelines

When creating new test files:
1. **Feature file first**: Define the behavior in Gherkin
2. **Step definitions**: Create the glue code class
3. **Step library**: Create the reusable step class
4. **Page objects**: Create page objects as needed

Always place files in the correct package/directory according to the project structure.

## Testing Philosophy

- **BDD-first**: Write feature files before implementation
- **Reusability**: Step libraries should be reusable across multiple scenarios
- **Maintainability**: Page objects centralize element locators
- **Readability**: Tests should be readable by product owners and QA
- **Reliability**: Use appropriate waits, avoid hard-coded sleeps

## Date Context

Current date: November 6, 2025
- Use current versions of dependencies when suggesting updates
- Consider modern Java 21+ features
- Suggest current best practices for 2025

---

When generating code, always respect this architecture and follow these patterns consistently.

