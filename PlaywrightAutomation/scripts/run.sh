#!/usr/bin/env bash
set -euo pipefail

: "${HEADLESS:=true}"
: "${API_BASE_URL:=https://jsonplaceholder.typicode.com}"

export HEADLESS API_BASE_URL

echo "Installing Playwright browsers..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

echo "Running smoke tests (headless)..."
mvn clean verify -Dtest=SmokeTestRunner -P headless

echo "Generating and serving Allure report..."
mvn allure:serve
