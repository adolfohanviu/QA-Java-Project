package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qa.pages.LoginPage;
import com.qa.utils.BrowserContextManager;
import com.qa.utils.ConfigManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * LoginStepDefinitions - Step definitions for login scenarios
 */
public class LoginStepDefinitions {
    private static final Logger logger = LogManager.getLogger(LoginStepDefinitions.class);
    private LoginPage loginPage;

    @Given("User navigates to the login page")
    public void user_navigates_to_login_page() {
        BrowserContextManager.createPage();
        loginPage = new LoginPage();
        loginPage.navigateTo(ConfigManager.getBaseURL());
        logger.info("Navigated to login page");
    }

    @When("User enters username {string}")
    public void user_enters_username(String username) {
        loginPage.enterUsername(username);
    }

    @When("User enters password {string}")
    public void user_enters_password(String password) {
        loginPage.enterPassword(password);
    }

    @When("User clicks the login button")
    public void user_clicks_login_button() {
        loginPage.clickLoginButton();
    }

    @When("User logs in with credentials {string} and {string}")
    public void user_logs_in_with_credentials(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("User should see the products page")
    public void user_should_see_products_page() {
        // Wait for products to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("Thread interrupted", e);
        }
        String currentUrl = loginPage.getCurrentURL();
        Assert.assertTrue("Should be on inventory page", currentUrl.contains("inventory"));
    }

    @Then("User should see error message {string}")
    public void user_should_see_error_message(String expectedMessage) {
        Assert.assertTrue("Error message should be displayed", loginPage.isErrorMessageDisplayed());
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue("Error message should contain: " + expectedMessage, actualMessage.contains(expectedMessage));
    }
}
