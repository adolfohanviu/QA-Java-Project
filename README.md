# Playwright Java QA Automation Framework

Enterprise-style QA automation framework built with Java 21, Playwright, Cucumber BDD, REST Assured, and Allure.

Designed to demonstrate senior SDET practices:
- clean Page Object Model design
- UI and API suite separation
- centralized constants and configuration
- headless-first execution
- CI/CD-ready workflow orchestration

---

## Why this project stands out

- **Unified framework, separated test domains**: UI and API are in one codebase but executed as independent suites.
- **Portfolio-ready architecture**: reusable base classes, centralized selectors/constants, and layered configuration.
- **Deterministic runs**: explicit headless execution and robust waits reduce flaky outcomes.
- **Reporting and diagnostics**: Allure artifacts + structured logs + Cucumber JSON reports.
- **CI/CD integration**: smoke, regression, and all-tests workflows with artifact upload.

---

## Tech stack

- **Language**: Java 21
- **Build tool**: Maven
- **UI automation**: Microsoft Playwright (Java)
- **API automation**: REST Assured
- **BDD layer**: Cucumber + JUnit 4
- **Reporting**: Allure + Cucumber HTML/JSON
- **Logging**: Log4j2
- **Config**: Typesafe Config (HOCON)
- **CI/CD**: GitHub Actions

---

## Project structure

```text
qa-automation-platform-playwright-java/
├── .github/workflows/
│   ├── smoke-tests.yml
│   ├── regression-tests.yml
│   └── all-tests.yml
├── src/main/java/com/qa/
│   ├── api/APIClient.java
│   ├── pages/
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   ├── ProductPage.java
│   │   └── CartPage.java
│   └── utils/
│       ├── BrowserContextManager.java
│       ├── ConfigManager.java
│       ├── CommonUtils.java
│       └── TestConstants.java
├── src/test/java/com/qa/
│   ├── runners/
│   │   ├── SmokeTestRunner.java
│   │   ├── RegressionTestRunner.java
│   │   ├── UITestRunner.java
│   │   ├── APITestRunner.java
│   │   └── BaseTestRunner.java
│   └── stepdefs/
│       ├── Hooks.java
│       ├── LoginStepDefinitions.java
│       ├── ProductStepDefinitions.java
│       └── APIStepDefinitions.java
├── src/test/resources/
│   ├── features/
│   ├── data/
│   ├── application.conf
│   ├── application-dev.conf
│   ├── application-prod.conf
│   └── log4j2.xml
├── pom.xml
└── README.md
```

---

## Test strategy

### Suite separation

- **UI suite**: `UITestRunner` runs all non-API scenarios (`not @api`)
- **API suite**: `APITestRunner` runs only API scenarios (`@api`)
- **Regression suite**: `RegressionTestRunner` runs `@regression`
- **Smoke suite**: `SmokeTestRunner` runs `@smoke`
- **Full suite**: `BaseTestRunner` runs all scenarios

### Design principles

- One owner for browser lifecycle (`Hooks` + `BrowserContextManager`)
- Selectors centralized in `TestConstants`
- Config values loaded via `ConfigManager` with env override support
- Headless-first execution for stable local and CI parity

---

## Prerequisites

- Java 21
- Maven 3.6+
- Git

Verify:

```bash
java -version
mvn -version
```

---

## Setup

### 1) Clone

```bash
git clone https://github.com/adolfohanviu/qa-automation-platform-playwright-java.git
cd qa-automation-platform-playwright-java
```

### 2) Install dependencies

```bash
mvn clean install -DskipTests
```

### 3) Install Playwright browsers

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### 4) Configure environment

Use environment variables or `.vscode/settings.json` local settings.
Never commit credentials.

---

## Running tests (headless recommended)

### Most reliable command (any OS)

```bash
mvn clean verify -Dtest=BaseTestRunner -Dbrowser.headless=true
```

### By suite

```bash
# Smoke
mvn clean test -Dtest=SmokeTestRunner -Dbrowser.headless=true

# Regression
mvn clean test -Dtest=RegressionTestRunner -Dbrowser.headless=true

# UI only
mvn clean test -Dtest=UITestRunner -Dbrowser.headless=true

# API only
mvn clean test -Dtest=APITestRunner -Dbrowser.headless=true

# Full suite
mvn clean test -Dtest=BaseTestRunner -Dbrowser.headless=true
```

### By tag

```bash
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@api"
mvn clean test -Dtest=BaseTestRunner -Dcucumber.filter.tags="@smoke"
```

---

## Reporting

### Allure

```bash
mvn allure:report
mvn allure:serve
```

Primary outputs:
- `target/allure-results`
- `target/cucumber-reports/cucumber.html`
- `target/cucumber-reports/*.json`

---

## Configuration

Main files:
- `application.conf` (defaults)
- `application-dev.conf` (dev overrides)
- `application-prod.conf` (prod overrides)

Common runtime variables:
- `TEST_ENV`
- `BASE_URL`
- `API_BASE_URL`
- `BROWSER_TYPE`
- `HEADLESS`
- `LOG_LEVEL`
- `TEST_STANDARD_USER`
- `TEST_STANDARD_PASSWORD`

---

## CI/CD workflows

### `smoke-tests.yml`
- Trigger: pull requests
- Scope: smoke checks

### `regression-tests.yml`
- Trigger: push to `main`/`develop`, scheduled weekly
- Scope: regression set

### `all-tests.yml`
- Trigger: push, pull request, daily schedule
- Scope: **parallel jobs**
  - `ui-test` job runs `UITestRunner`
  - `api-test` job runs `APITestRunner`

Required repository secrets:
- `TEST_STANDARD_USER`
- `TEST_STANDARD_PASSWORD`
- `SLACK_WEBHOOK` (optional, regression notifications)

---

## Quality practices implemented

- Centralized selectors/constants in `TestConstants`
- Page Object Model with reusable `BasePage`
- Explicit headless JVM property in local + CI commands
- Structured assertions and scenario-level hooks
- Failure evidence (screenshot + final URL attachment)

---

## Portfolio summary

This project demonstrates practical SDET ownership across:
- framework architecture
- UI and API automation
- reliability improvements (flakiness reduction)
- CI orchestration and reporting
- documentation and maintainability

---

## License

MIT
