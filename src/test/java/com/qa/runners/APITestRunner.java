package com.qa.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * APITestRunner — runs only API scenarios (@api).
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.stepdefs"},
        tags = "@api",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "json:target/cucumber-reports/api-cucumber.json"
        },
        monochrome = true
)
public class APITestRunner {
}
