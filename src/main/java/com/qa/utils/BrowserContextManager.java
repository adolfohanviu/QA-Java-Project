package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * BrowserContextManager handles Playwright browser and context lifecycle.
 * Usage: call initBrowser() → createContext() → createPage() before tests,
 * and closeBrowser() in teardown (which also calls ThreadLocal.remove()).
 */
public class BrowserContextManager {

    private static final Logger logger = LogManager.getLogger(BrowserContextManager.class);

    private static final ThreadLocal<Playwright> playwrightHolder = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserHolder = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageHolder = new ThreadLocal<>();

    private BrowserContextManager() {
        // Utility class — do not instantiate
    }

    /**
     * Initialize Playwright and launch a browser for the current thread.
     * Browser type and headless mode are read from ConfigManager.
     *
     * @throws RuntimeException if browser initialization fails
     */
    public static void initBrowser() {
        try {
            Playwright playwright = Playwright.create();
            playwrightHolder.set(playwright);

            String browserType = ConfigManager.getBrowserType();
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(ConfigManager.isHeadless());

            Browser browser = switch (browserType.toLowerCase()) {
                case "firefox" -> playwright.firefox().launch(options);
                case "webkit"  -> playwright.webkit().launch(options);
                default        -> playwright.chromium().launch(options);
            };
            browserHolder.set(browser);

            logger.info("Browser launched: {} (headless: {}, thread: {})",
                    browserType, ConfigManager.isHeadless(), Thread.currentThread().threadId());
        } catch (Exception e) {
            logger.error("Failed to initialize browser", e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }

    /**
     * Create a new isolated browser context for the current thread.
     * Initializes the browser first if not already done.
     */
    public static void createContext() {
        if (browserHolder.get() == null) {
            initBrowser();
        }
        BrowserContext context = browserHolder.get().newContext();
        contextHolder.set(context);
        logger.info("Browser context created (thread: {})", Thread.currentThread().threadId());
    }

    /**
     * Create a new page within the current thread's browser context.
     * Creates a context first if not already done.
     */
    public static void createPage() {
        if (contextHolder.get() == null) {
            createContext();
        }
        Page page = contextHolder.get().newPage();
        pageHolder.set(page);
        logger.info("New page created (thread: {})", Thread.currentThread().threadId());
    }

    /**
     * Retrieve the current thread's Page instance.
     * Creates the full browser/context/page chain if not yet initialized.
     *
     * @return Current thread-local Playwright Page
     */
    public static Page getPage() {
        if (pageHolder.get() == null) {
            createPage();
        }
        return pageHolder.get();
    }

    /**
     * Navigate to a URL on the current thread's page.
     *
     * @param url Full URL to navigate to
     */
    public static void navigateTo(String url) {
        getPage().navigate(url);
        logger.info("Navigated to: {}", url);
    }

    /**
     * Close and nullify the current thread's page.
     */
    public static void closePage() {
        Page page = pageHolder.get();
        if (page != null) {
            page.close();
            pageHolder.remove();
            logger.info("Page closed (thread: {})", Thread.currentThread().threadId());
        }
    }

    /**
     * Close and nullify the current thread's browser context.
     */
    public static void closeContext() {
        BrowserContext context = contextHolder.get();
        if (context != null) {
            context.close();
            contextHolder.remove();
            logger.info("Context closed (thread: {})", Thread.currentThread().threadId());
        }
    }

    /**
     * Close all browser resources for the current thread and clean up ThreadLocals.
     * Must be called in test teardown to prevent memory leaks.
     */
    public static void closeBrowser() {
        closePage();
        closeContext();

        Browser browser = browserHolder.get();
        if (browser != null) {
            browser.close();
            browserHolder.remove();
        }

        Playwright playwright = playwrightHolder.get();
        if (playwright != null) {
            playwright.close();
            playwrightHolder.remove();
        }

        logger.info("Browser fully closed and ThreadLocals cleaned (thread: {})",
                Thread.currentThread().threadId());
    }

    /**
     * Reset browser state: close page+context and create fresh ones.
     * Useful for mid-suite isolation without relaunching the browser.
     */
    public static void resetBrowser() {
        closePage();
        closeContext();
        createContext();
        createPage();
        logger.info("Browser reset (thread: {})", Thread.currentThread().threadId());
    }
}
