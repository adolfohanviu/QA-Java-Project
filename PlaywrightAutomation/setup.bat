@echo off
REM Playwright Automation Framework - Setup Script for Windows

echo =========================================
echo Playwright Automation Framework Setup
echo =========================================
echo.

REM Check Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed. Please install Java 21 LTS or higher.
    exit /b 1
)

for /f tokens^=2 %%J in ('java -version 2^>^&1 ^| find /i "version"') do set JAVA_VERSION=%%J
echo [OK] Java version: %JAVA_VERSION%

REM Check Maven
mvn -v >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed. Please install Maven 3.6 or higher.
    exit /b 1
)

echo [OK] Maven is installed

REM Install dependencies
echo.
echo Installing Maven dependencies...
call mvn clean install -DskipTests

REM Install Playwright browsers
echo.
echo Installing Playwright browsers...
call mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

REM Create directories
echo.
echo Creating required directories...
if not exist "target\screenshots" mkdir target\screenshots
if not exist "target\logs" mkdir target\logs
if not exist "allure-results" mkdir allure-results
if not exist "allure-history" mkdir allure-history

echo.
echo [SUCCESS] Setup completed successfully!
echo.
echo Running smoke tests...
call mvn clean test -Dtest=SmokeTestRunner

echo.
echo To view Allure reports:
echo    mvn allure:serve
echo.
pause
