package com.qa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qa.utils.BrowserContextManager;
import com.qa.utils.ConfigManager;

/**
 * BasePage is the parent class for all page objects
 * Provides common methods for element interaction
 */
public class BasePage {
    protected Page page;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected static final int TIMEOUT = ConfigManager.getTimeout();

    public BasePage() {
        this.page = BrowserContextManager.getPage();
    }

    /**
     * Navigate to a URL
     */
    public void navigateTo(String url) {
        page.navigate(url);
        logger.info("Navigated to: " + url);
    }

    /**
     * Click on element
     */
    public void click(String selector) {
        try {
            page.locator(selector).click();
            logger.info("Clicked on element: " + selector);
        } catch (Exception e) {
            logger.error("Failed to click element: " + selector, e);
            throw e;
        }
    }

    /**
     * Type text into element
     */
    public void typeText(String selector, String text) {
        try {
            page.locator(selector).fill(text);
            logger.info("Typed text in element: " + selector);
        } catch (Exception e) {
            logger.error("Failed to type in element: " + selector, e);
            throw e;
        }
    }

    /**
     * Get text from element
     */
    public String getText(String selector) {
        try {
            String text = page.locator(selector).textContent();
            logger.info("Get text from element: " + selector + " = " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + selector, e);
            throw e;
        }
    }

    /**
     * Wait for element to be visible
     */
    public void waitForElement(String selector) {
        try {
            page.locator(selector).waitFor();
            logger.info("Element is visible: " + selector);
        } catch (Exception e) {
            logger.error("Failed to find element: " + selector, e);
            throw e;
        }
    }

    /**
     * Check if element is visible
     */
    public boolean isElementVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (Exception e) {
            logger.warn("Element not visible: " + selector);
            return false;
        }
    }

    /**
     * Get attribute value
     */
    public String getAttribute(String selector, String attribute) {
        try {
            String value = page.locator(selector).getAttribute(attribute);
            logger.info("Get attribute " + attribute + " from element: " + selector);
            return value;
        } catch (Exception e) {
            logger.error("Failed to get attribute: " + selector, e);
            throw e;
        }
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        String title = page.title();
        logger.info("Page title: " + title);
        return title;
    }

    /**
     * Get current URL
     */
    public String getCurrentURL() {
        String url = page.url();
        logger.info("Current URL: " + url);
        return url;
    }

    /**
     * Execute JavaScript
     */
    public Object executeScript(String script) {
        try {
            Object result = page.evaluate(script);
            logger.info("JavaScript executed");
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript", e);
            throw e;
        }
    }

    /**
     * Scroll element into view
     */
    public void scrollIntoView(String selector) {
        try {
            page.locator(selector).scrollIntoViewIfNeeded();
            logger.info("Scrolled element into view: " + selector);
        } catch (Exception e) {
            logger.error("Failed to scroll element: " + selector, e);
            throw e;
        }
    }

    /**
     * Take screenshot
     */
    public void takeScreenshot(String fileName) {
        try {
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get("target/screenshots/" + fileName + ".png")));
            logger.info("Screenshot taken: " + fileName);
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
    }

    /**
     * Hover over element
     */
    public void hover(String selector) {
        try {
            page.locator(selector).hover();
            logger.info("Hovered over element: " + selector);
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + selector, e);
            throw e;
        }
    }

    /**
     * Select option from dropdown
     */
    public void selectDropdownOption(String selector, String value) {
        try {
            page.locator(selector).selectOption(value);
            logger.info("Selected option: " + value + " from: " + selector);
        } catch (Exception e) {
            logger.error("Failed to select option: " + selector, e);
            throw e;
        }
    }
}
