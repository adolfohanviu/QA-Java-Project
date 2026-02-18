package com.qa.stepdefs;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.utils.BrowserContextManager;
import com.qa.utils.CommonUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;

/**
 * Hooks - Setup and teardown methods for test scenarios
 * Manages browser lifecycle and test context
 * Captures screenshots on failure and logs scenario status to Allure
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    /**
     * Before hook - runs before each scenario
     * Initializes browser, context, and page for test execution
     */
    @Before
    public void setUp() {
        logger.info("========== Setting up test environment ==========");
        Allure.step("Initializing browser");
        BrowserContextManager.initBrowser();
        BrowserContextManager.createContext();
        BrowserContextManager.createPage();
        logger.info("Test environment setup completed");
    }

    /**
     * After hook - runs after each scenario
     * Captures screenshots on failure, logs scenario status
     * Closes browser and cleans up resources
     * 
     * @param scenario Cucumber scenario object with status and name
     */
    @After
    public void tearDown(io.cucumber.java.Scenario scenario) {
        logger.info("========== Tearing down test environment ==========");
        try {
            // Log scenario status to Allure
            if (scenario.isFailed()) {
                String failureMessage = String.format("Scenario failed: %s", scenario.getName());
                Allure.step(failureMessage);
                logger.error(failureMessage);
                
                // Capture screenshot on failure
                String screenshotPath = CommonUtils.takeScreenshot(
                        String.format("failed_%d", System.currentTimeMillis()));
                if (screenshotPath != null) {
                    try {
                        Allure.addAttachment("Failure Screenshot", "image/png",
                                Files.newInputStream(Paths.get(screenshotPath)), ".png");
                        logger.info(String.format("Screenshot attached to Allure report: %s", screenshotPath));
                    } catch (Exception attachmentException) {
                        logger.warn(String.format("Failed to attach screenshot to Allure: %s", screenshotPath), 
                                attachmentException);
                    }
                }
            } else {
                String successMessage = String.format("Scenario passed: %s", scenario.getName());
                Allure.step(successMessage);
                logger.info(successMessage);
            }

            // Attach current URL for debugging
            try {
                String currentUrl = CommonUtils.getCurrentURL();
                Allure.addAttachment("Current URL", "text/plain", currentUrl);
                logger.info(String.format("Current URL attached to report: %s", currentUrl));
            } catch (Exception urlException) {
                logger.warn("Failed to attach current URL to Allure report", urlException);
            }
            
            // Close browser and cleanup resources
            BrowserContextManager.closeBrowser();
            logger.info("Test environment teardown completed");
        } catch (Exception e) {
            logger.error("Error during teardown", e);
            try {
                BrowserContextManager.closeBrowser();
            } catch (Exception closeException) {
                logger.error("Failed to close browser during error recovery", closeException);
            }
        }
    }
}