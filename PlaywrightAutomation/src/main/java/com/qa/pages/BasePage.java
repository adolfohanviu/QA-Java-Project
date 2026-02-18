package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.qa.utils.BrowserContextManager;
import com.qa.utils.ConfigManager;
import com.qa.utils.TestConstants;

/**
 * BasePage is the parent class for all page objects
 * Provides common methods for element interaction with proper exception handling
 * Uses TestConstants for timeout and retry values
 */
public class BasePage {
    protected Page page;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected static final int TIMEOUT = ConfigManager.getTimeout();
    private static final int RETRY_COUNT = TestConstants.MAX_RETRY_ATTEMPTS;
    private static final int RETRY_DELAY_MS = TestConstants.RETRY_DELAY_MS;

    public BasePage() {
        this.page = BrowserContextManager.getPage();
    }

    /**
     * Navigate to a URL
     * @param url The URL to navigate to
     * @throws PlaywrightException if navigation fails
     */
    public void navigateTo(String url) {
        try {
            page.navigate(url);
            logger.info(String.format("Navigated to: %s", url));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to navigate to URL: %s", url), e);
            throw e;
        }
    }

    /**
     * Click on element with retry logic
     * @param selector CSS selector for the element
     * @throws PlaywrightException if click fails after retries
     */
    public void click(String selector) {
        for (int attempt = 1; attempt <= RETRY_COUNT; attempt++) {
            try {
                page.locator(selector).click();
                logger.info(String.format("Clicked on element: %s", selector));
                return;
            } catch (PlaywrightException e) {
                if (attempt == RETRY_COUNT) {
                    logger.error(String.format("Failed to click element after %d attempts: %s", RETRY_COUNT, selector), e);
                    throw e;
                } else {
                    logger.warn(String.format("Click attempt %d/%d failed for %s, retrying...", attempt, RETRY_COUNT, selector));
                    sleep(RETRY_DELAY_MS * attempt); // Exponential backoff
                }
            }
        }
    }

    /**
     * Type text into element
     * @param selector CSS selector for the element
     * @param text Text to type
     * @throws PlaywrightException if typing fails
     */
    public void typeText(String selector, String text) {
        try {
            page.locator(selector).fill(text);
            logger.info(String.format("Typed text in element: %s", selector));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to type in element: %s", selector), e);
            throw e;
        }
    }

    /**
     * Get text from element
     * @param selector CSS selector for the element
     * @return Text content or empty string if null
     * @throws PlaywrightException if retrieval fails
     */
    public String getText(String selector) {
        try {
            String text = page.locator(selector).textContent();
            if (text == null || text.isBlank()) {
                logger.warn(String.format("Element text is empty: %s", selector));
                return "";
            }
            logger.info(String.format("Get text from element: %s = %s", selector, text));
            return text;
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to get text from element: %s", selector), e);
            throw e;
        }
    }

    /**
     * Wait for element to be visible
     * @param selector CSS selector for the element
     * @throws PlaywrightException if element not found
     */
    public void waitForElement(String selector) {
        try {
            page.locator(selector).waitFor();
            logger.info(String.format("Element is visible: %s", selector));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to find element within timeout: %s", selector), e);
            throw e;
        }
    }

    /**
     * Check if element is visible
     * @param selector CSS selector for the element
     * @return true if element is visible, false otherwise
     */
    public boolean isElementVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (PlaywrightException e) {
            logger.debug(String.format("Element not visible: %s", selector), e);
            return false;
        }
    }

    /**
     * Get attribute value from element
     * @param selector CSS selector for the element
     * @param attribute Attribute name
     * @return Attribute value or empty string if null
     * @throws PlaywrightException if retrieval fails
     */
    public String getAttribute(String selector, String attribute) {
        try {
            String value = page.locator(selector).getAttribute(attribute);
            if (value == null || value.isBlank()) {
                logger.warn(String.format("Attribute '%s' is empty for element: %s", attribute, selector));
                return "";
            }
            logger.info(String.format("Get attribute %s from element: %s = %s", attribute, selector, value));
            return value;
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to get attribute '%s' from element: %s", attribute, selector), e);
            throw e;
        }
    }

    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        String title = page.title();
        logger.info(String.format("Page title: %s", title));
        return title;
    }

    /**
     * Get current URL
     * @return Current page URL
     */
    public String getCurrentURL() {
        String url = page.url();
        logger.info(String.format("Current URL: %s", url));
        return url;
    }

    /**
     * Execute JavaScript code
     * @param script JavaScript code to execute
     * @return Result of JavaScript execution
     * @throws PlaywrightException if execution fails
     */
    public Object executeScript(String script) {
        try {
            Object result = page.evaluate(script);
            logger.info("JavaScript executed successfully");
            return result;
        } catch (PlaywrightException e) {
            logger.error("Failed to execute JavaScript", e);
            throw e;
        }
    }

    /**
     * Scroll element into view
     * @param selector CSS selector for the element
     * @throws PlaywrightException if scrolling fails
     */
    public void scrollIntoView(String selector) {
        try {
            page.locator(selector).scrollIntoViewIfNeeded();
            logger.info(String.format("Scrolled element into view: %s", selector));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to scroll element: %s", selector), e);
            throw e;
        }
    }

    /**
     * Take screenshot of current page
     * @param fileName Name of the screenshot file (without extension)
     */
    public void takeScreenshot(String fileName) {
        try {
            java.nio.file.Path screenshotDir = java.nio.file.Paths.get("target/screenshots");
            java.nio.file.Files.createDirectories(screenshotDir);
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(java.nio.file.Paths.get(String.format("target/screenshots/%s.png", fileName))));
            logger.info(String.format("Screenshot taken: %s.png", fileName));
        } catch (java.io.IOException | PlaywrightException e) {
            logger.error(String.format("Failed to take screenshot: %s", fileName), e);
        }
    }

    /**
     * Hover over element
     * @param selector CSS selector for the element
     * @throws PlaywrightException if hover fails
     */
    public void hover(String selector) {
        try {
            page.locator(selector).hover();
            logger.info(String.format("Hovered over element: %s", selector));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to hover over element: %s", selector), e);
            throw e;
        }
    }

    /**
     * Select option from dropdown
     * Ensures element is visible and stable before interaction
     * @param selector CSS selector for the dropdown
     * @param value Value of option to select
     * @throws PlaywrightException if selection fails
     */
    public void selectDropdownOption(String selector, String value) {
        try {
            page.locator(selector).waitFor();
            page.locator(selector).scrollIntoViewIfNeeded();
            page.locator(selector).selectOption(value);
            logger.info(String.format("Selected option: %s from: %s", value, selector));
        } catch (PlaywrightException e) {
            logger.error(String.format("Failed to select option '%s' from dropdown: %s", value, selector), e);
            throw e;
        }
    }

    /**
     * Utility method for sleeping (use sparingly - prefer Playwright waits)
     * @param milliseconds Time to sleep in milliseconds
     */
    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.warn(String.format("Thread interrupted during sleep of %d ms", milliseconds), e);
            Thread.currentThread().interrupt();
        }
    }
}