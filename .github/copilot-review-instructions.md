# Copilot Pull Request Review Instructions

## Review Focus Areas

When reviewing pull requests for this test automation framework, focus on:

### 1. Architecture & Layering
- ✅ **Verify three-layer pattern is followed**: Feature Files → Step Definitions → Step Libraries → Page Objects
- ❌ **Flag violations**: Step definitions calling page objects directly (must go through step libraries)
- ✅ **Check separation of concerns**:
  - Page Objects: Only element location and basic interactions
  - Step Libraries: Business logic, assertions, reusable actions
  - Step Definitions: Thin glue code that delegates to step libraries

### 2. Code Quality & Standards

#### Java Code
- Verify Java 21 conventions are followed
- Check for proper use of Serenity BDD annotations (`@Steps`, `@DefaultUrl`, `@FindBy`)
- Ensure AssertJ is used for assertions (not JUnit assertions)
- Verify proper exception handling with meaningful error messages
- Check for JavaDoc on complex methods and business logic
- Flag any hard-coded waits (use `waitFor()` instead)
- Verify proper use of `PageObject` base class methods

#### Feature Files
- Gherkin syntax is correct and readable
- Scenarios are written from a business perspective (readable by non-technical stakeholders)
- Step parameters use double quotes for strings
- Feature files follow snake_case naming convention
- Scenarios have clear Given/When/Then structure

#### Naming Conventions
- **Packages**: All lowercase, dot-separated
- **Classes**: PascalCase
- **Methods**: camelCase (except Cucumber step methods which use underscores)
- **Feature files**: snake_case with `.feature` extension

### 3. Dependencies & Imports
- No unnecessary dependencies added to `build.gradle`
- Prefer Serenity BDD methods over raw Selenium when available
- Correct import packages:
  - `net.serenitybdd.*` for Serenity features
  - `io.cucumber.*` for Cucumber features
  - `org.openqa.selenium.*` for Selenium features
- AssertJ static imports are used correctly

### 3a. Dependency Version & Vulnerability Management
- **Version Currency**: All dependencies should use recent stable versions (not outdated by more than 6 months)
- **Version Pinning**: All versions must be explicitly specified (no dynamic versions like `4.+` or `latest`)
- **⚠️ VERIFY DON'T ASSUME**: Always query Maven Central or official sources to verify actual latest versions - don't guess or use outdated information
- **Compatibility**: Verify dependency versions are compatible with each other
- **Security Vulnerabilities**: Check for known CVEs in dependencies
- **⚠️ CHECK OVERRIDES**: When adding dependency overrides to fix vulnerabilities, verify the override versions don't introduce NEW vulnerabilities
- **Major Version Alignment**: Related libraries should use compatible major versions (e.g., all Serenity BDD libs on same version)
- **EOL Checking**: Verify dependencies are not end-of-life or abandoned
- **Changelog Review**: For version updates, review breaking changes and migration guides
- **Test After Updates**: Always run full test suite after dependency updates to verify no regressions

#### Tools for Dependency Checking
```bash
# Check for outdated dependencies
./gradlew dependencyUpdates

# Run security vulnerability scan (if configured)
./gradlew dependencyCheckAnalyze

# List all dependencies and their resolved versions
./gradlew dependencies

# Verify against Maven Central (example)
curl -s "https://search.maven.org/solrsearch/select?q=g:GROUP_ID+AND+a:ARTIFACT_ID&rows=1&wt=json"
```

#### Version Alignment Guidelines
- **Serenity BDD**: All `net.serenity-bdd:*` artifacts must use the same version
- **Cucumber**: All `io.cucumber:*` artifacts must use compatible versions
- **JUnit Platform**: All `org.junit.platform:*` and `org.junit.jupiter:*` must align
- **Selenium**: Verify WebDriverManager version supports the Selenium version

#### Red Flags for Dependencies
- ❌ Libraries with known high/critical CVEs
- ❌ Dependencies not updated in 2+ years (potential abandonment)
- ❌ Mixing major versions of the same library family
- ❌ Using pre-release or snapshot versions in production code
- ❌ Transitive dependency conflicts

### 4. Test Reliability & Best Practices
- No `Thread.sleep()` or hard-coded delays (use explicit waits)
- Proper use of `waitFor()` methods from Serenity PageObject
- Element locators are robust (prefer stable locators like `id`, `name` over fragile `xpath`)
- Tests are idempotent and can run independently
- No test data hard-coded in page objects or step libraries (should be in feature files or test data files)

### 5. Serenity Configuration
- Changes to `serenity.properties` are intentional and documented
- WebDriver configuration is appropriate (remote vs local)
- Timeout values are reasonable

### 6. File Organization
- Files are in correct packages according to project structure:
  - Page Objects: `.../pages/`
  - Step Libraries: `.../steps/`
  - Step Definitions: `.../stepdefinitions/`
  - Test Runners: `.../runners/`
  - Feature Files: `src/test/resources/features/`

### 7. Completeness
- If a new feature file is added, corresponding step definitions, step libraries, and page objects are included
- New test scenarios have proper assertions (not just actions)
- Changes include both positive and negative test cases where appropriate

## Specific Red Flags

### ❌ Anti-Patterns to Flag
1. **Layer bypassing**: Step definitions calling page objects directly
2. **God classes**: Page objects or step libraries doing too much
3. **Hard-coded waits**: Use of `Thread.sleep()`
4. **Fragile locators**: Complex XPath expressions that will break easily
5. **Business logic in page objects**: Should be in step libraries
6. **Assertions in page objects**: Should be in step libraries
7. **Missing waits**: Not waiting for elements before interaction
8. **Duplicate step definitions**: Same Gherkin step defined multiple times
9. **Unused imports**: Clean up unused import statements
10. **Raw exceptions**: Throwing generic `Exception` instead of specific types
11. **Windows CMD syntax in Git Bash**: Using `2>nul`, `NUL`, etc. that create unwanted files
12. **Workarounds in .gitignore**: Adding entries to hide mistakes rather than fixing root causes

### ✅ Good Practices to Encourage
1. **Reusable step libraries**: Methods that can be used across multiple scenarios
2. **Clear method names**: Self-documenting code
3. **Stable locators**: Using `id`, `name`, `data-testid` attributes
4. **Descriptive Gherkin**: Business-readable scenarios
5. **Proper error messages**: Assertions with meaningful failure messages
6. **DRY principle**: No duplicate code across page objects or step libraries
7. **Single Responsibility**: Each class has one clear purpose
8. **Proper scope**: Private fields, public methods where appropriate
9. **Correct shell commands**: POSIX/Unix commands for Git Bash (e.g., `mkdir -p`, `/dev/null`)
10. **Clean .gitignore**: Only intentional exclusions, no workarounds for command mistakes

## Review Checklist

For each PR, verify:
- [ ] Code follows the three-layer architecture pattern
- [ ] All new classes are in the correct package/directory
- [ ] Naming conventions are followed consistently
- [ ] No hard-coded waits or sleeps
- [ ] Proper use of Serenity BDD annotations and methods
- [ ] AssertJ assertions used (not JUnit)
- [ ] Feature files are readable by non-technical stakeholders
- [ ] No duplicate step definitions
- [ ] Appropriate JavaDoc for complex logic
- [ ] No unnecessary dependencies added
- [ ] **All dependency versions are explicitly specified (no dynamic versions)**
- [ ] **Dependencies are up-to-date (within 6 months of latest stable)**
- [ ] **No known security vulnerabilities in dependencies**
- [ ] **Related library versions are aligned (e.g., all Serenity BDD on same version)**
- [ ] Tests are reliable and can run independently
- [ ] Proper error handling and meaningful error messages
- [ ] No business logic or assertions in page objects
- [ ] Shell commands use POSIX/Unix syntax (not Windows CMD)
- [ ] No unwanted files created (like `nul`) and no .gitignore workarounds for them

## Severity Guidelines

### 🔴 Critical (Request changes)
- Layer pattern violations
- Hard-coded waits/sleeps
- Tests that will be flaky or unreliable
- Missing assertions in test scenarios
- Business logic in page objects
- **Dependencies with known high/critical security vulnerabilities**
- **Using dynamic dependency versions (e.g., `4.+`, `latest`)**
- Shell commands that create unwanted files (e.g., using `2>nul` in Git Bash)
- .gitignore entries added to hide command mistakes

### 🟡 Important (Strong suggestion)
- Fragile locators that should be improved
- Missing JavaDoc on complex methods
- Code duplication that should be refactored
- Non-standard naming conventions
- Missing error handling
- **Dependencies outdated by more than 6 months**
- **Version misalignment in related libraries**
- **Dependencies with low/medium severity vulnerabilities**

### 🟢 Minor (Nice-to-have)
- Code style improvements
- Additional test coverage suggestions
- Performance optimizations
- Documentation enhancements

## Tone & Communication

- Be constructive and educational (explain *why* something should change)
- Provide specific examples or suggest alternatives
- Acknowledge good practices when you see them
- Link to relevant sections in `.github/copilot-instructions.md` when applicable
- Consider the context: Is this a prototype, spike, or production code?

## Example Review Comments

### Good Comment:
> "This step definition is calling the page object directly. Following our three-layer pattern, it should call a method in `GoogleSearchSteps` (step library) instead, which then calls the page object. This keeps the step definition thin and makes the logic reusable. See `.github/copilot-instructions.md` section 3 for details."

### Avoid:
> "Wrong pattern."

---

Focus on maintaining code quality while fostering a collaborative and learning-oriented review culture.

