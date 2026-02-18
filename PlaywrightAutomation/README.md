# Playwright Automation Framework

Comprehensive end-to-end test automation framework built with Playwright, Java, and Cucumber. Designed for UI and API testing with Allure reporting, headless execution, and CI/CD integration.

> **Status**: ✅ CI/CD pipelines configured and passing - All Tests, Smoke Tests, Regression Tests

## Features

✅ **Page Object Model (POM)** - Clean, maintainable page objects  
✅ **Cucumber BDD** - Business-readable test scenarios  
✅ **Playwright** - Modern browser automation with Java 21  
✅ **Allure Reports** - Beautiful test reporting  
✅ **API Testing** - REST API testing integrated  
✅ **Multi-browser Support** - Chrome, Firefox, WebKit  
✅ **CI/CD** - GitHub Actions workflows with Java 21 LTS  
✅ **Logging** - Log4j2 comprehensive logging  
✅ **Configuration Management** - Environment-specific configs  

## Project Structure

```
PlaywrightAutomation/
├── src/
│   ├── main/java/com/qa/
│   │   ├── pages/          # Page Object Models
│   │   ├── api/            # API testing utilities
│   │   └── utils/          # Common utilities
│   └── test/
│       ├── java/com/qa/
│       │   ├── stepdefs/   # Cucumber step definitions
│       │   └── runners/    # Test runners
│       └── resources/
│           ├── features/   # Cucumber feature files
│           └── config/     # Configuration files
├── .github/workflows/      # GitHub Actions CI/CD
├── pom.xml                 # Maven configuration
└── README.md
```

## Prerequisites

- Java 21 LTS (Recommended) or Java 17+
- Maven 3.6+
- Git

## Setup

### 1. Clone Repository
```bash
git clone <repository-url>
cd PlaywrightAutomation
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Download Playwright Browsers
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Smoke Tests
```bash
mvn clean test -Dtest=SmokeTestRunner
```

### One-command setup + run

Windows PowerShell:
```powershell
./scripts/run.ps1
```

macOS/Linux:
```bash
./scripts/run.sh
```

### Run Headless (recommended)
```bash
HEADLESS=true mvn clean test -Dtest=SmokeTestRunner
```

Or with the Maven profile:
```bash
mvn clean test -P headless -Dtest=SmokeTestRunner
```

Windows PowerShell:
```powershell
$env:HEADLESS="true"
mvn clean test -Dtest=SmokeTestRunner
```

### Run Regression Tests
```bash
mvn clean test -Dtest=RegressionTestRunner
```

### Run Specific Feature
```bash
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@smoke"
```

CI uses `mvn verify` to ensure the full Maven lifecycle runs (with tests).

## Configuration

Configuration is managed via HOCON files in `src/test/resources/config/`:
- `application.conf` - Default configuration
- `application-dev.conf` - Development environment
- `application-prod.conf` - Production environment

### Environment Variables
```bash
BASE_URL=https://www.saucedemo.com/
BROWSER_TYPE=chromium
HEADLESS=true
API_BASE_URL=https://jsonplaceholder.typicode.com
TEST_ENV=dev
```

## Reports

### Allure Report
After test execution:
```bash
mvn allure:report
mvn allure:serve
```

Tip: Use `mvn allure:serve` to avoid "Loading..." or 404s when opening the report.

## Test Data Fixtures

API payload fixtures live in `src/test/resources/data/api-users.json` and can be used in feature steps:
```
When I make a POST request to "/users" with fixture "userBasic"
```

The Allure report shows:
- Test execution statistics
- Failed tests with detailed logs
- Test timeline and duration
- Screenshots and artifacts
- Environment details
- Historical trends

Report location: `target/site/allure-maven-plugin/index.html`

## CI/CD Pipelines

### All Tests Workflow
- Triggers: push, pull_request, scheduled (daily 2 AM UTC)
- Java version: 21 LTS
- Runs all test suites
- Generates Allure reports

### Smoke Tests Workflow
- Triggers: pull_request
- Runs only smoke tests
- Fast feedback for PRs

### Regression Tests Workflow
- Triggers: push to main/develop, scheduled (Friday 10 PM UTC)
- Comprehensive test suite
- Slack notifications

## Page Objects

### LoginPage
- `enterUsername(String username)`
- `enterPassword(String password)`
- `clickLoginButton()`
- `login(String username, String password)`
- `getErrorMessage()`
- `isErrorMessageDisplayed()`

### ProductPage
- `getProductTitle(int index)`
- `getProductPrice(int index)`
- `addProductToCart(int index)`
- `getCartCount()`
- `sortProducts(String sortOption)`
- `removeProductFromCart(String productName)`

### CartPage
- `getCartItemCount()`
- `clickCheckoutButton()`
- `getCartItemPrice(int index)`
- `continueShopping()`
- `isCartEmpty()`

## API Testing

Default API base URL (demo): `https://jsonplaceholder.typicode.com`

Run API-only tests:
```bash
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags=@api
```

Windows PowerShell:
```powershell
mvn clean test -Dtest=BaseTestRunner "-Dcucumber.filter.tags=@api"
```

Note: API tests require outbound network access to the demo endpoint.

### APIClient Methods
- `addHeader(String key, String value)`
- `addHeaders(Map<String, String> headers)`
- `setBody(Object body)`
- `addPathParam(String key, Object value)`
- `addQueryParam(String key, Object value)`
- `get(String endpoint)`
- `post(String endpoint)`
- `put(String endpoint)`
- `delete(String endpoint)`
- `patch(String endpoint)`

## Utilities

### BrowserContextManager
- `initBrowser()`
- `createContext()`
- `createPage()`
- `getPage()`
- `navigateTo(String url)`
- `closePage()`
- `closeContext()`
- `closeBrowser()`
- `resetBrowser()`

### ConfigManager
- Manages environment-specific configurations
- Supports HOCON configuration files
- Environment variable override support

### CommonUtils
- `pause(long milliseconds)`
- `takeScreenshot(String fileName)`
- `getPageTitle()`
- `getCurrentURL()`
- `generateRandomEmail()`
- `generateRandomString(int length)`

## Stability & Artifacts

- Failed tests are retried once automatically (`rerunFailingTestsCount=1`).
- On failures, screenshots are captured to `target/screenshots` and attached to Allure.

## Cucumber Features

### Tags
- `@smoke` - Smoke/critical tests
- `@regression` - Full regression suite
- `@api` - API tests
- `@wip` - Work in progress (skipped by default)

### Feature Files
- `Login.feature` - Login scenarios
- `Shopping.feature` - Product and shopping scenarios
- `API.feature` - API testing scenarios

## Logging

Logs are configured via `log4j2.xml`:
- Console output
- Rolling file appenders
- Error log segregation
- Automatic log rotation

Log files location: `target/logs/`

## Best Practices

1. **Page Objects** - Keep page objects focused on single page functionality
2. **Step Definitions** - Use clear, business-readable language
3. **Assertions** - Use meaningful assertion messages
4. **Waits** - Use explicit waits instead of Thread.sleep()
5. **Logging** - Log all important actions and errors
6. **Screenshots** - Take screenshots on failures for debugging

## Troubleshooting

### Playwright Browsers Not Found
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Tests Timing Out
Increase timeout in `application.conf`:
```
timeout {
  default = 60000  # 60 seconds
  wait = 20000     # 20 seconds
}
```

### Configuration Not Loading
Check `TEST_ENV` environment variable and ensure config file exists in `src/test/resources/config/`

## Author

SDET - QA Automation Engineer
