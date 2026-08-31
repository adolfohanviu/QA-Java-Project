package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import com.qa.pages.{ClassName}Page;
import com.qa.utils.TestConstants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;

/**
 * {ClassName}StepDefinitions — Cucumber steps for {flow name}.
 *
 * Never manage browser lifecycle here — that belongs to Hooks only.
 * This class only instantiates and calls the page object.
 */
public class {ClassName}StepDefinitions {

    private static final Logger logger = LogManager.getLogger({ClassName}StepDefinitions.class);
    private {ClassName}Page {instanceName}Page;

    @Given("{Gherkin step text}")
    @Step("{Allure step label}")
    public void given_step() {
        {instanceName}Page = new {ClassName}Page();
    }

    @When("{Gherkin step text}")
    @Step("{Allure step label}")
    public void when_step() {
        {instanceName}Page.someAction();
    }

    @Then("{Gherkin step text}")
    @Step("{Allure step label}")
    public void then_step() {
        Assert.assertTrue(
                TestConstants.AssertionMessages.WRONG_PAGE,
                {instanceName}Page.isSomeElementVisible());
        logger.info("Verified expected state on {ClassName}Page");
    }
}
