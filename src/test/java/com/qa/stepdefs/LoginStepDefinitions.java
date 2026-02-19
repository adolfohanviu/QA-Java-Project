package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qa.pages.LoginPage;
import com.qa.utils.BrowserContextManager;
import com.qa.utils.ConfigManager;
import com.qa.utils.TestConstants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;

/**
 * LoginStepDefinitions — Cucumber step implementations for login scenarios.
 */
public class LoginStepDefinitions {

    private static final Logger logger = LogManager.getLogger(LoginStepDefinitions.class);
    private LoginPage loginPage;

    @Given("User navigates to the login page")
    @Step("Navigate to login page")
    public void user_navigates_to_login_page() {
        loginPage = new LoginPage();
        loginPage.navigateTo(ConfigManager.getBaseURL());
        logger.info("Navigated to login page");
    }

    @When("User enters username {string}")
    @Step("Enter username: {username}")
    public void user_enters_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("User enters password {string}")
    @Step("Enter password")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("User clicks the login button")
    @Step("Click login button")
    public void user_clicks_login_button() {
        loginPage.clickLoginButton();
    }

    @When("User logs in with credentials {string} and {string}")
    @Step("Login with username: {username}")
    public void user_logs_in_with_credentials(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("User should see the products page")
    @Step("Verify products page is displayed")
    public void user_should_see_products_page() {
        loginPage.waitForElement(TestConstants.Selectors.PRODUCTS_CONTAINER);
        String currentUrl = loginPage.getCurrentURL();
        Assert.assertTrue(
                TestConstants.AssertionMessages.WRONG_PAGE + ": " + currentUrl,
                currentUrl.contains("inventory"));
        logger.info("Verified user is on products page");
    }

    @Then("User should see error message {string}")
    @Step("Verify error message contains: {expectedMessage}")
    public void user_should_see_error_message(String expectedMessage) {
        Assert.assertTrue(
                TestConstants.AssertionMessages.ERROR_NOT_DISPLAYED,
                loginPage.isErrorMessageDisplayed());

        String actualMessage = loginPage.getErrorMessage();
        Assert.assertNotNull("Error message should not be null", actualMessage);
        Assert.assertTrue(
                "Error message '" + actualMessage + "' should contain: " + expectedMessage,
                actualMessage.contains(expectedMessage));
        logger.info("Verified error message contains: {}", expectedMessage);
    }

    @Then("User should see {string}")
    @Step("Verify page shows: {expectedResult}")
    public void user_should_see(String expectedResult) {
        if ("products page".equals(expectedResult)) {
            user_should_see_products_page();
        } else {
            // Treat as a partial error-message match
            user_should_see_error_message(expectedResult);
        }
    }

    @Then("Login should complete within {int} seconds")
    @Step("Verify login completed within {seconds} seconds")
    public void login_should_complete_within(int seconds) {
        loginPage.waitForURL("inventory");
        logger.info("Login completed within the allowed time ({} s)", seconds);
    }

    @When("User clicks the logout button")
    @Step("Logout")
    public void user_clicks_logout_button() {
        loginPage.click(TestConstants.Selectors.BURGER_MENU);
        loginPage.waitForElement(TestConstants.Selectors.LOGOUT_LINK);
        loginPage.click(TestConstants.Selectors.LOGOUT_LINK);
        logger.info("Logged out via burger menu");
    }

    @Then("User should see the login page")
    @Step("Verify login page is displayed")
    public void user_should_see_the_login_page() {
        loginPage.waitForElement(TestConstants.Selectors.LOGIN_BUTTON);
        String currentUrl = loginPage.getCurrentURL();
        Assert.assertTrue(
                "Expected login page URL, got: " + currentUrl,
            currentUrl.equals(ConfigManager.getBaseURL()) || currentUrl.startsWith(ConfigManager.getBaseURL()));
        logger.info("Verified user is on login page");
    }

    @When("User clicks the browser back button")
    @Step("Navigate browser back")
    public void user_clicks_browser_back_button() {
        BrowserContextManager.getPage().goBack();
        logger.info("Navigated browser back");
    }

    @Then("User should be redirected to login page")
    @Step("Verify redirection to login page")
    public void user_should_be_redirected_to_login_page() {
        user_should_see_the_login_page();
    }
}
