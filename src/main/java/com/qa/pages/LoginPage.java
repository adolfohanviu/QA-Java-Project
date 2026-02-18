package com.qa.pages;

import io.qameta.allure.Step;

/**
 * LoginPage - Page Object for login functionality
 * Tests against Sauce Labs demo site
 */
public class LoginPage extends BasePage {
    // Locators
    private static final String USERNAME_INPUT = "[data-test='username']";
    private static final String PASSWORD_INPUT = "[data-test='password']";
    private static final String LOGIN_BUTTON = "[data-test='login-button']";
    private static final String ERROR_MESSAGE = "[data-test='error']";

    /**
     * Enter username into login form
     * @param username Username to enter
     */
    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        typeText(USERNAME_INPUT, username);
    }

    /**
     * Enter password into login form
     * @param password Password to enter
     */
    @Step("Enter password")
    public void enterPassword(String password) {
        typeText(PASSWORD_INPUT, password);
    }

    /**
     * Click login button
     */
    @Step("Click login button")
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    /**
     * Login with provided credentials
     * @param username Username to use for login
     * @param password Password to use for login
     */
    @Step("Login with username: {username}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info(String.format("Login performed with username: %s", username));
    }

    /**
     * Get error message displayed on login form
     * @return Error message text
     */
    @Step("Get error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    /**
     * Check if error message is displayed
     * @return true if error message visible, false otherwise
     */
    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(ERROR_MESSAGE);
    }
}