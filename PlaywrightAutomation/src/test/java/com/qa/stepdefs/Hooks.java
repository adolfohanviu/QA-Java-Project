package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.utils.BrowserContextManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * Hooks - Setup and teardown methods for test scenarios
 * Manages browser lifecycle and test context
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);

    /**
     * Before hook - runs before each scenario
     */
    @Before
    public void setUp() {
        logger.info("========== Setting up test environment ==========");
        BrowserContextManager.initBrowser();
        BrowserContextManager.createContext();
        BrowserContextManager.createPage();
    }

    /**
     * After hook - runs after each scenario
     */
    @After
    public void tearDown() {
        logger.info("========== Tearing down test environment ==========");
        try {
            // Take screenshot on failure if needed
            BrowserContextManager.closeBrowser();
        } catch (Exception e) {
            logger.error("Error during teardown", e);
        }
    }
}
