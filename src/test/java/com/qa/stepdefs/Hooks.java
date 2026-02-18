package com.qa.stepdefs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.utils.BrowserContextManager;
import com.qa.utils.CommonUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

/**
 * Hooks — Cucumber Before/After lifecycle management.
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    /**
     * Runs before every scenario: initialise a fresh browser, context, and page.
     * The ThreadLocal design in BrowserContextManager makes this safe for
     * parallel execution.
     */
    @Before(order = 0)
    public void setUp() {
        logger.info("===== Setting up test environment (thread: {}) =====",
                Thread.currentThread().getId());
        Allure.step("Initializing browser");
        BrowserContextManager.initBrowser();
        BrowserContextManager.createContext();
        BrowserContextManager.createPage();
    }

    /**
     * Runs after every scenario: captures a screenshot on failure, attaches
     * the current URL, then tears down all browser resources.
     */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        logger.info("===== Tearing down test environment (thread: {}) =====",
                Thread.currentThread().getId());
        try {
            if (scenario.isFailed()) {
                logger.error("Scenario FAILED: {}", scenario.getName());
                Allure.step("Scenario failed — capturing screenshot");

                Optional<String> screenshotPath =
                        CommonUtils.takeScreenshot("failed_" + System.currentTimeMillis());

                screenshotPath.ifPresentOrElse(
                        path -> {
                            try {
                                Allure.addAttachment("Failure Screenshot", "image/png",
                                        Files.newInputStream(Paths.get(path)), ".png");
                            } catch (IOException e) {
                                logger.warn("Could not attach screenshot to Allure report", e);
                            }
                        },
                        () -> logger.warn("Screenshot capture failed; no attachment added")
                );
            } else {
                logger.info("Scenario PASSED: {}", scenario.getName());
                Allure.step("Scenario passed");
            }

            // Attach the final URL regardless of outcome
            try {
                String currentUrl = BrowserContextManager.getPage().url();
                Allure.addAttachment("Final URL", "text/plain", currentUrl);
            } catch (Exception e) {
                logger.debug("Could not capture final URL during teardown", e);
            }

        } catch (Exception e) {
            logger.error("Unexpected error during teardown", e);
        } finally {
            // Always close browser — also removes ThreadLocals to prevent leaks
            BrowserContextManager.closeBrowser();
        }
    }
}
