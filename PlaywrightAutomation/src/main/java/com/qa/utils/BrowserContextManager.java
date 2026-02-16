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
 */
public class BrowserContextManager {
    private static final Logger logger = LogManager.getLogger(BrowserContextManager.class);
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    /**
     * Initialize browser instance
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

            logger.info("Browser launched: " + browserType);
        } catch (Exception e) {
            logger.error("Failed to initialize browser", e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    /**
     * Create a new browser context
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
     */
    public static Page getPage() {
        if (page == null) {
            createPage();
        }
        return page;
    }

    /**
     * Navigate to a URL
     */
    public static void navigateTo(String url) {
        getPage().navigate(url);
        logger.info("Navigated to: " + url);
    }

    /**
     * Close the current page
     */
    public static void closePage() {
        if (page != null) {
            page.close();
            page = null;
            logger.info("Page closed");
        }
    }

    /**
     * Close the current context
     */
    public static void closeContext() {
        if (context != null) {
            context.close();
            context = null;
            logger.info("Context closed");
        }
    }

    /**
     * Close the browser
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
     * Reset browser state (new context and page)
     */
    public static void resetBrowser() {
        closePage();
        closeContext();
        createContext();
        createPage();
        logger.info("Browser reset");
    }
}
