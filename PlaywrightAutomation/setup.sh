#!/bin/bash

# Playwright Automation Framework - Setup Script

echo "🚀 Starting Playwright Automation Framework Setup..."

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21 LTS or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "✅ Java version: $JAVA_VERSION"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

MVN_VERSION=$(mvn -v | grep "Apache Maven")
echo "✅ $MVN_VERSION"

# Install dependencies
echo "📦 Installing Maven dependencies..."
mvn clean install -DskipTests

# Install Playwright browsers
echo "🌐 Installing Playwright browsers..."
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# Create directories
echo "📁 Creating required directories..."
mkdir -p target/screenshots
mkdir -p target/logs
mkdir -p allure-results
mkdir -p allure-history

echo ""
echo "✅ Setup completed successfully!"
echo ""
echo "Running smoke tests..."
mvn clean test -Dtest=SmokeTestRunner

echo ""
echo "📊 To view Allure reports:"
echo "   mvn allure:serve"
echo ""
