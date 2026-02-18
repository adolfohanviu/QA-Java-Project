$ErrorActionPreference = "Stop"

if (-not $env:HEADLESS) {
    $env:HEADLESS = "true"
}

if (-not $env:API_BASE_URL) {
    $env:API_BASE_URL = "https://jsonplaceholder.typicode.com"
}

Write-Host "Installing Playwright browsers..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

Write-Host "Running smoke tests (headless)..."
mvn clean verify -Dtest=SmokeTestRunner -P headless

Write-Host "Generating and serving Allure report..."
mvn allure:serve
