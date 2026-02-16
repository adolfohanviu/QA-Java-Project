package com.qa.pages;

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
     * Enter username
     */
    public void enterUsername(String username) {
        typeText(USERNAME_INPUT, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        typeText(PASSWORD_INPUT, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    /**
     * Login with credentials
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Login performed with username: " + username);
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(ERROR_MESSAGE);
    }
}
