package com.qa.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * UITestRunner — runs only UI scenarios (excludes @api).
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.stepdefs"},
        tags = "not @api",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "json:target/cucumber-reports/ui-cucumber.json"
        },
        monochrome = true
)
public class UITestRunner {
}
