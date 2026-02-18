package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * BrowserContextManager handles Playwright browser and context lifecycle
 * Provides singleton pattern for browser instance management
 * Manages initialization, creation, and cleanup of browser resources
 */
public class BrowserContextManager {
    private static final Logger logger = LogManager.getLogger(BrowserContextManager.class);
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    /**
     * Initialize Playwright browser instance
     * Creates browser based on configuration (chromium, firefox, webkit)
     * Sets headless mode based on configuration
     * 
     * @throws RuntimeException if browser initialization fails
     */
    public static void initBrowser() {
        try {
            playwright = Playwright.create();
            String browserType = ConfigManager.getBrowserType();
            
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(ConfigManager.isHeadless());

            browser = switch (browserType.toLowerCase()) {
                case "firefox" -> playwright.firefox().launch(options);
                case "webkit" -> playwright.webkit().launch(options);
                default -> playwright.chromium().launch(options);
            };

            logger.info(String.format("Browser launched: %s (headless: %s)", 
                    browserType, ConfigManager.isHeadless()));
        } catch (Exception e) {
            logger.error("Failed to initialize browser", e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    /**
     * Create a new browser context
     * Must be called after initBrowser()
     * Browser context provides isolated environment for pages
     * 
     * @throws RuntimeException if browser not initialized
     */
    public static void createContext() {
        if (browser == null) {
            initBrowser();
        }
        context = browser.newContext();
        logger.info("Browser context created");
    }

    /**
     * Create a new page in the current context
     * Must be called after createContext()
     * Page is the interface for interacting with browser
     * 
     * @throws RuntimeException if context not initialized
     */
    public static void createPage() {
        if (context == null) {
            createContext();
        }
        page = context.newPage();
        logger.info("New page created");
    }

    /**
     * Get the current page instance
     * Creates page automatically if not already initialized
     * 
     * @return Current Playwright Page object
     * @throws RuntimeException if page creation fails
     */
    public static Page getPage() {
        if (page == null) {
            createPage();
        }
        return page;
    }

    /**
     * Navigate to a URL
     * 
     * @param url Full URL to navigate to
     */
    public static void navigateTo(String url) {
        getPage().navigate(url);
        logger.info(String.format("Navigated to: %s", url));
    }

    /**
     * Close the current page
     * Releases page resources
     */
    public static void closePage() {
        if (page != null) {
            page.close();
            page = null;
            logger.info("Page closed");
        }
    }

    /**
     * Close the current browser context
     * Releases context resources
     */
    public static void closeContext() {
        if (context != null) {
            context.close();
            context = null;
            logger.info("Context closed");
        }
    }

    /**
     * Close the browser and all instances
     * Cleans up all Playwright resources
     * Call this in test teardown
     */
    public static void closeBrowser() {
        closePage();
        closeContext();
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
        logger.info("Browser closed");
    }

    /**
     * Reset browser state
     * Closes current page and context, creates new ones
     * Useful for test isolation between scenarios
     */
    public static void resetBrowser() {
        closePage();
        closeContext();
        createContext();
        createPage();
        logger.info("Browser reset");
    }
}