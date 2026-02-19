package com.qa.pages;

import com.qa.utils.TestConstants;

import io.qameta.allure.Step;

/**
 * LoginPage — Page Object for the SauceDemo login screen.
 */
public class LoginPage extends BasePage {

    private static final String USERNAME_INPUT = TestConstants.Selectors.USERNAME_INPUT;
    private static final String PASSWORD_INPUT = TestConstants.Selectors.PASSWORD_INPUT;
    private static final String LOGIN_BUTTON   = TestConstants.Selectors.LOGIN_BUTTON;
    private static final String ERROR_MESSAGE  = TestConstants.Selectors.ERROR_MESSAGE;

    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        typeText(USERNAME_INPUT, username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        typeText(PASSWORD_INPUT, password);
    }

    @Step("Click login button")
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }

    @Step("Login with username: {username}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        logger.info("Login attempted with username: {}", username);
    }

    @Step("Get login error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    @Step("Check if error message is displayed")
    public boolean isErrorMessageDisplayed() {
        return isElementVisible(ERROR_MESSAGE);
    }
}
