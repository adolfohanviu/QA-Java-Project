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
 * LoginStepDefinitions - Step definitions for login scenarios
 * Uses TestConstants for centralized test data and selectors
 */
public class LoginStepDefinitions {
    private static final Logger logger = LogManager.getLogger(LoginStepDefinitions.class);
    private LoginPage loginPage;

    @Given("User navigates to the login page")
    @Step("Navigate to login page")
    public void user_navigates_to_login_page() {
        BrowserContextManager.createPage();
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
        // Wait for products page to load using Playwright's built-in waits
        loginPage.waitForElement(TestConstants.Selectors.PRODUCTS_CONTAINER);
        
        String currentUrl = loginPage.getCurrentURL();
        Assert.assertTrue(
                String.format(TestConstants.AssertionMessages.WRONG_PAGE + ": %s", currentUrl),
                currentUrl.contains("inventory"));
        logger.info("Successfully verified products page is displayed");
    }

    @Then("User should see error message {string}")
    @Step("Verify error message contains: {expectedMessage}")
    public void user_should_see_error_message(String expectedMessage) {
        Assert.assertTrue(
                "Error message should be displayed on login page",
                loginPage.isErrorMessageDisplayed());
        
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertNotNull("Error message should not be null", actualMessage);
        Assert.assertTrue(
                String.format("Error message '%s' should contain: %s", actualMessage, expectedMessage),
                actualMessage.contains(expectedMessage));
        logger.info(String.format("Successfully verified error message contains: %s", expectedMessage));
    }
}