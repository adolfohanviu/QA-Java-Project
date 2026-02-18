package com.qa.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * RegressionTestRunner — runs scenarios tagged with @regression.
 * Scheduled weekly and on pushes to main/develop.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.stepdefs"},
        tags = "@regression",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "json:target/cucumber-reports/regression-cucumber.json"
        },
        monochrome = true
)
public class RegressionTestRunner {
}
