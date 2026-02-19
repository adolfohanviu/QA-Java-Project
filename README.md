# Playwright Automation Framework

Comprehensive end-to-end test automation framework built with Playwright, Java 21, and Cucumber BDD. Designed for UI and API testing with Allure reporting, headless execution, and CI/CD integration.

## Features

✅ **Page Object Model (POM)** — Clean, maintainable page objects with Allure `@Step` annotations  
✅ **Cucumber BDD** — Business-readable test scenarios  
✅ **Playwright 1.49** — Modern browser automation with auto-wait built in  
✅ **Thread-Safe Execution** — `ThreadLocal` browser isolation for safe parallel runs  
✅ **Allure Reports** — Screenshots on failure, URL attachment, step-level reporting  
✅ **API Testing** — REST API testing via REST Assured with response body assertions  
✅ **Multi-browser** — Chromium, Firefox, WebKit  
✅ **CI/CD** — GitHub Actions workflows (smoke, regression, full suite)  
✅ **Log4j2 Logging** — Console + rolling file + error-only appender  
✅ **Environment Config** — HOCON-based, multi-environment with env variable override  
✅ **Secure Credentials** — No passwords in source code; resolved from environment variables  

---

## Project Structure

```
PlaywrightAutomation/
├── .env.example                        # Template for local environment variables
├── .github/
│   └── workflows/
│       ├── all-tests.yml               # Full suite — push, PR, daily schedule
│       ├── smoke-tests.yml             # Smoke only — every PR
│       └── regression-tests.yml        # Regression — push to main + weekly
├── src/
│   ├── main/java/com/qa/
│   │   ├── api/
│   │   │   └── APIClient.java          # Fluent REST Assured HTTP client
│   │   ├── pages/
│   │   │   ├── BasePage.java           # Base class with common Playwright helpers
│   │   │   ├── LoginPage.java          # Login screen interactions
│   │   │   ├── ProductPage.java        # Product listing and cart interactions
│   │   │   └── CartPage.java           # Shopping cart interactions
│   │   └── utils/
│   │       ├── BrowserContextManager.java  # ThreadLocal Playwright lifecycle manager
│   │       ├── ConfigManager.java          # HOCON config with env var override
│   │       ├── CommonUtils.java            # Screenshot, random data helpers
│   │       └── TestConstants.java          # Selectors, timeouts, assertion messages
│   └── test/
│       ├── java/com/qa/
│       │   ├── runners/
│       │   │   ├── BaseTestRunner.java      # All tests
│       │   │   ├── SmokeTestRunner.java     # @smoke tag only
│       │   │   └── RegressionTestRunner.java # @regression tag only
│       │   └── stepdefs/
│       │       ├── Hooks.java               # Before/After — sole owner of browser lifecycle
│       │       ├── LoginStepDefinitions.java
│       │       ├── ProductStepDefinitions.java
│       │       └── APIStepDefinitions.java
│       └── resources/
│           ├── features/
│           │   ├── login.feature
│           │   ├── product-shopping.feature
│           │   └── api-users.feature
│           ├── data/
│           │   └── api-users.json       # API request fixtures
│           ├── application.conf         # Default configuration
│           ├── application-dev.conf     # Dev overrides (headed, DEBUG logging)
│           ├── application-prod.conf    # Prod overrides (headless, WARN logging)
│           └── log4j2.xml
├── pom.xml
└── README.md
```

---

## Prerequisites

- **Java 21 LTS** (Oracle JDK or Temurin)
- **Maven 3.6+**
- **Git**

Verify your setup:
```bash
java -version   # should show 21.x
mvn -version    # should show 3.6+
```

---

## Setup

### 1. Clone the repository
```bash
git clone <repository-url>
cd PlaywrightAutomation
```

### 2. Configure local credentials
Copy the environment template and fill in your values:
```bash
cp .env.example .env
```

> ⚠️ `.env` is gitignored and must never be committed. It contains real credentials.

For **VS Code**, add environment variables to `.vscode/settings.json`:
```json
{
    "java.configuration.updateBuildConfiguration": "automatic",
    "maven.terminal.customEnv": [
        { "environmentVariable": "TEST_ENV", "value": "dev" },
        { "environmentVariable": "BROWSER_TYPE", "value": "chromium" },
        { "environmentVariable": "HEADLESS", "value": "true" },
        { "environmentVariable": "BASE_URL", "value": "https://www.saucedemo.com/" },
        { "environmentVariable": "TEST_STANDARD_USER", "value": "standard_user" },
        { "environmentVariable": "TEST_STANDARD_PASSWORD", "value": "secret_sauce" }
    ]
}
```

> ⚠️ Add `.vscode/settings.json` to `.gitignore` if it contains real passwords.

### 3. Install dependencies
```bash
mvn clean install -DskipTests
```

### 4. Install Playwright browsers
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

---

## Running Tests

### Headless mode (recommended)

**Windows PowerShell:**
```powershell
$env:HEADLESS="true"
mvn clean test -Dtest=SmokeTestRunner
```

**macOS / Linux:**
```bash
HEADLESS=true mvn clean test -Dtest=SmokeTestRunner
```

**Via Maven system property (any OS, most reliable):**
```bash
mvn clean test -Dtest=SmokeTestRunner -Dbrowser.headless=true
```

**Alternative system property:**
```bash
mvn clean test -Dtest=SmokeTestRunner -DHEADLESS=true
```

**Via Maven profile:**
```bash
mvn clean test -P headless -Dtest=SmokeTestRunner
```

---

### Run smoke tests (fast — ~1 min)
```bash
mvn clean test -Dtest=SmokeTestRunner
```

### Run regression tests
```bash
mvn clean test -Dtest=RegressionTestRunner
```

### Run all tests
```bash
mvn clean test -Dtest=BaseTestRunner
```

### Run UI tests only
```bash
mvn clean test -Dtest=UITestRunner -Dbrowser.headless=true
```

### Run API tests only
```bash
mvn clean test -Dtest=APITestRunner -Dbrowser.headless=true
```

### Run by tag
```bash
# Any OS
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@smoke"
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@api"

# Windows PowerShell (quote the tag expression)
mvn clean test -Dtest=BaseTestRunner "-Dcucumber.filter.tags=@smoke"
```

### Run API tests only
```bash
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@api"
```

## Test Strategy

- `UITestRunner` executes all non-API scenarios (`not @api`) for browser-based validation.
- `APITestRunner` executes only API scenarios (`@api`) for service-level validation.
- `all-tests` workflow runs UI and API suites as separate parallel jobs for clearer reporting and faster feedback.

---

## Configuration

Configuration is managed via HOCON files in `src/test/resources/`:

| File | Purpose |
|---|---|
| `application.conf` | Default values, all keys defined here |
| `application-dev.conf` | Dev overrides: headed browser, DEBUG logging |
| `application-prod.conf` | Prod overrides: headless, WARN logging |

Select environment via the `TEST_ENV` variable (default: `dev`):
```bash
TEST_ENV=prod mvn clean test -Dtest=SmokeTestRunner
```

### Environment variables

All config values can be overridden at runtime:

| Variable | Default | Description |
|---|---|---|
| `TEST_ENV` | `dev` | Environment: `dev` or `prod` |
| `BASE_URL` | `https://www.saucedemo.com/` | Application under test |
| `BROWSER_TYPE` | `chromium` | Browser: `chromium`, `firefox`, `webkit` |
| `HEADLESS` | `true` (CI) / `false` (dev) | Headed or headless mode |
| `API_BASE_URL` | `https://jsonplaceholder.typicode.com` | API base URL |
| `LOG_LEVEL` | `INFO` | Log level |
| `TEST_STANDARD_USER` | `standard_user` | Test username |
| `TEST_STANDARD_PASSWORD` | *(set via secret)* | Test password — never hardcode |

### Timeout configuration

Edit `src/test/resources/application.conf`:
```hocon
timeout {
  default = 30000  # Page load / navigation (ms)
  wait    = 10000  # Element wait timeout (ms)
}
```

---

## Reports

### Generate and open Allure report
```bash
mvn allure:report
mvn allure:serve      # Opens browser automatically — avoids "Loading..." issues
```

Report location: `target/site/allure-maven-plugin/index.html`

The Allure report includes:
- Pass/fail statistics per suite
- Step-level breakdown with `@Step` annotations
- Screenshot attachment on every failed scenario
- Final page URL attached to every scenario
- Test duration and timeline view
- Historical trend (when run in CI)

---

## Cucumber Tags

| Tag | When it runs |
|---|---|
| `@smoke` | Every pull request (SmokeTestRunner) |
| `@regression` | Push to main/develop + weekly (RegressionTestRunner) |
| `@api` | Included in regression; run independently with tag filter |
| `@wip` | Not wired to any runner — use for in-progress work |

---

## Page Objects

### BasePage
Parent class for all page objects. All methods use Playwright's built-in auto-wait.

| Method | Description |
|---|---|
| `navigateTo(String url)` | Navigate to URL |
| `click(String selector)` | Click element |
| `typeText(String selector, String text)` | Fill input |
| `getText(String selector)` | Get element text content |
| `getTextByIndex(String selector, int index)` | Get nth element text (zero-based) |
| `getAttribute(String selector, String attr)` | Get element attribute |
| `waitForElement(String selector)` | Wait for element to be visible |
| `waitForURL(String fragment)` | Wait for URL to contain fragment |
| `isElementVisible(String selector)` | Check visibility without throwing |
| `countElements(String selector)` | Count matching elements |
| `selectDropdownOption(String selector, String option)` | Select by label |
| `getCurrentURL()` | Get current page URL |

### LoginPage
| Method | Description |
|---|---|
| `login(String username, String password)` | Full login sequence |
| `enterUsername(String username)` | Fill username field |
| `enterPassword(String password)` | Fill password field |
| `clickLoginButton()` | Submit login form |
| `getErrorMessage()` | Get error message text |
| `isErrorMessageDisplayed()` | Check if error is visible |

### ProductPage
| Method | Description |
|---|---|
| `addProductToCart(int oneBasedIndex)` | Add product by 1-based index |
| `getProductTitle(int index)` | Get product name (zero-based) |
| `getProductPrice(int index)` | Get price string (zero-based) |
| `parsePrice(String priceText)` | Parse "$9.99" → 9.99 |
| `getProductCount()` | Total products on page |
| `getCartCount()` | Cart badge count (0 if empty) |
| `sortProducts(String sortOption)` | Sort by label text |
| `removeProductFromCart(String dataTestName)` | Remove by data-test suffix |

### CartPage
| Method | Description |
|---|---|
| `getCartItemCount()` | Number of line items |
| `getCartItemName(int index)` | Item name (zero-based) |
| `getCartItemPrice(int index)` | Item price as double (zero-based) |
| `isCartEmpty()` | True when count is 0 |
| `clickCheckoutButton()` | Proceed to checkout |
| `continueShopping()` | Return to product page |

---

## API Testing

Base URL: `https://jsonplaceholder.typicode.com` (configurable via `API_BASE_URL`)

### APIClient methods
| Method | Description |
|---|---|
| `get(String endpoint)` | HTTP GET |
| `post(String endpoint)` | HTTP POST |
| `put(String endpoint)` | HTTP PUT |
| `patch(String endpoint)` | HTTP PATCH |
| `delete(String endpoint)` | HTTP DELETE |
| `addHeader(String key, String value)` | Add single header |
| `addHeaders(Map<String, String> headers)` | Add multiple headers |
| `setBody(Object body)` | Set request body (serialized to JSON) |
| `addPathParam(String key, Object value)` | Add path parameter |
| `addQueryParam(String key, Object value)` | Add query parameter |

All methods except HTTP verbs return `this` for fluent chaining:
```java
response = new APIClient()
    .addHeader("Authorization", "Bearer token")
    .setBody(requestBody)
    .post("/users");
```

### API fixtures
Store reusable request payloads in `src/test/resources/data/` as JSON:
```gherkin
When I make a POST request to "/users" with fixture "api-users"
```

---

## Utilities

### BrowserContextManager
Thread-safe browser lifecycle manager using `ThreadLocal` — safe for parallel execution.

| Method | Description |
|---|---|
| `initBrowser()` | Launch browser for current thread |
| `createContext()` | Create isolated browser context |
| `createPage()` | Open a new page |
| `getPage()` | Get current thread's page |
| `closeBrowser()` | Close all resources + remove ThreadLocals |
| `resetBrowser()` | Close and re-create context + page |

> **Note:** `Hooks.java` is the sole owner of the browser lifecycle. Never call `initBrowser()`, `createPage()`, or `closeBrowser()` from step definitions.

### ConfigManager
| Method | Description |
|---|---|
| `getBaseURL()` | Application base URL |
| `getBrowserType()` | Browser type string |
| `isHeadless()` | Headless flag (checks system property → env var → config) |
| `getTimeout()` | Default timeout (ms) |
| `getWaitTimeout()` | Element wait timeout (ms) |
| `getAPIBaseURL()` | API base URL |
| `getProperty(String key)` | Custom string property (returns `""` if missing, never `null`) |

### CommonUtils
| Method | Description |
|---|---|
| `takeScreenshot(String fileName)` | Capture screenshot → `Optional<String>` path |
| `getPageTitle()` | Current page title |
| `getCurrentURL()` | Current page URL |
| `generateRandomEmail()` | Unique test email |
| `generateRandomString(int length)` | Random alphanumeric string |
| `pause(long milliseconds)` | Sleep — use only as last resort |

---

## CI/CD

All workflows use `actions/checkout@v4`, `actions/setup-java@v4`, and `actions/upload-artifact@v4`.  
Credentials are injected via GitHub repository secrets — never stored in workflow files.

### Required secrets
| Secret | Description |
|---|---|
| `TEST_STANDARD_USER` | Test account username |
| `TEST_STANDARD_PASSWORD` | Test account password |
| `SLACK_WEBHOOK` | (Optional) Slack incoming webhook URL for regression notifications |

### Workflows

| Workflow | Trigger | Runner |
|---|---|---|
| `smoke-tests.yml` | Every pull request | `SmokeTestRunner` (`@smoke`) |
| `all-tests.yml` | Push, PR, daily 2 AM UTC | `BaseTestRunner` (all tags) |
| `regression-tests.yml` | Push to main/develop, Friday 10 PM UTC | `RegressionTestRunner` (`@regression`) |

---

## Logging

Log4j2 is configured in `src/test/resources/log4j2.xml`.

| Appender | Location | Level |
|---|---|---|
| Console | stdout | INFO (com.qa), WARN (others) |
| File | `target/logs/automation.log` | INFO — rolling daily / 10 MB |
| ErrorFile | `target/logs/error.log` | ERROR only |

---

## Troubleshooting

### Playwright browsers not found
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Dependency conflicts on `mvn install`
The Cucumber and Allure versions must stay in sync. Tested compatible versions:
- `cucumber` → `7.14.1`
- `allure-cucumber7-jvm` → `2.25.0`

Do not upgrade either independently.

### Tests timing out
Increase timeouts in `application.conf`:
```hocon
timeout {
  default = 60000
  wait    = 20000
}
```

### Configuration not loading
Ensure `TEST_ENV` matches an existing config file name (`application-dev.conf`, `application-prod.conf`). Config files must be at the **root** of `src/test/resources/` — not in a subfolder.

### Red underlines in VS Code after `mvn install`
Press `Ctrl+Shift+P` → **Java: Clean Language Server Workspace** → Restart.

### Browser opens in headed mode despite HEADLESS=true
System property takes priority over env var. Pass it directly:
```bash
mvn clean test -Dtest=SmokeTestRunner -DHEADLESS=true
```

---

## Best Practices

1. **Never call `createPage()` in step definitions** — `Hooks` owns the browser lifecycle
2. **Never hardcode credentials** — use `TestConstants.TestUsers` which reads from env vars
3. **Never use `Thread.sleep()`** — use `waitForElement()` or `waitForURL()` instead
4. **Use `nth(index)` not `:nth-child()`** — CSS child selectors break when DOM order changes
5. **Use `TestConstants`** for selectors and assertion messages to avoid scattered magic strings
6. **Use parameterised logging** — `logger.info("Value: {}", val)` not `"Value: " + val`
7. **Return `Optional`** from methods that may not produce a value (e.g. `takeScreenshot`)

---

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Write or update feature files and step definitions
3. Ensure all tests pass locally: `mvn clean test -Dtest=BaseTestRunner`
4. Generate and review the Allure report: `mvn allure:serve`
5. Open a pull request — smoke tests run automatically

---

## License

MIT License — see LICENSE file for details.

---

**Framework Version:** 1.0.0  
**Last Updated:** 2026-02-19  
**Java:** 21 LTS | **Playwright:** 1.49.0 | **Cucumber:** 7.14.1 | **Allure:** 2.25.0