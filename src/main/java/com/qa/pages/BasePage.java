package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.SelectOption;
import com.qa.utils.BrowserContextManager;
import com.qa.utils.ConfigManager;

import io.qameta.allure.Step;

/**
 * BasePage — parent class for all Page Objects.
 *
 * Provides safe, logged wrappers around common Playwright interactions.
 *
 */
public abstract class BasePage {

    protected final Page page;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);

    protected BasePage() {
        this.page = BrowserContextManager.getPage();
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    @Step("Navigate to {url}")
    public void navigateTo(String url) {
        page.navigate(url);
        logger.info("Navigated to: {}", url);
    }

    /** @return The current page URL. */
    public String getCurrentURL() {
        return page.url();
    }

    // -------------------------------------------------------------------------
    // Element interactions
    // -------------------------------------------------------------------------

    @Step("Click element: {selector}")
    public void click(String selector) {
        try {
            page.locator(selector).click();
            logger.info("Clicked: {}", selector);
        } catch (PlaywrightException e) {
            logger.error("Failed to click element: {}", selector, e);
            throw e;
        }
    }

    @Step("Type '{text}' into {selector}")
    public void typeText(String selector, String text) {
        try {
            page.locator(selector).fill(text);
            logger.info("Typed text into: {}", selector);
        } catch (PlaywrightException e) {
            logger.error("Failed to type in element: {}", selector, e);
            throw e;
        }
    }

    @Step("Get text from {selector}")
    public String getText(String selector) {
        try {
            String text = page.locator(selector).textContent();
            logger.info("Got text from {}: {}", selector, text);
            return text;
        } catch (PlaywrightException e) {
            logger.error("Failed to get text from element: {}", selector, e);
            throw e;
        }
    }

    @Step("Get text from nth({index}) element: {selector}")
    public String getTextByIndex(String selector, int index) {
        try {
            String text = page.locator(selector).nth(index).textContent();
            logger.info("Got text from {}[{}]: {}", selector, index, text);
            return text;
        } catch (PlaywrightException e) {
            logger.error("Failed to get text from {}[{}]", selector, index, e);
            throw e;
        }
    }

    @Step("Get attribute '{attribute}' from {selector}")
    public String getAttribute(String selector, String attribute) {
        try {
            String value = page.locator(selector).getAttribute(attribute);
            logger.info("Got attribute '{}' from {}: {}", attribute, selector, value);
            return value;
        } catch (PlaywrightException e) {
            logger.error("Failed to get attribute '{}' from {}", attribute, selector, e);
            throw e;
        }
    }

    @Step("Select option '{option}' in dropdown {selector}")
    public void selectDropdownOption(String selector, String option) {
        try {
            page.locator(selector).selectOption(new SelectOption().setLabel(option));
            logger.info("Selected '{}' in dropdown: {}", option, selector);
        } catch (PlaywrightException e) {
            logger.error("Failed to select option '{}' in dropdown {}", option, selector, e);
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // Waits & visibility
    // -------------------------------------------------------------------------

    @Step("Wait for element: {selector}")
    public void waitForElement(String selector) {
        try {
            page.locator(selector).waitFor(
                    new Locator.WaitForOptions()
                            .setTimeout(ConfigManager.getWaitTimeout()));
            logger.info("Element visible: {}", selector);
        } catch (PlaywrightException e) {
            logger.error("Timed out waiting for element: {}", selector, e);
            throw e;
        }
    }

    @Step("Wait for URL to contain: {urlFragment}")
    public void waitForURL(String urlFragment) {
        try {
            page.waitForURL("**" + urlFragment + "**",
                    new Page.WaitForURLOptions()
                            .setTimeout(ConfigManager.getTimeout()));
            logger.info("URL now contains: {}", urlFragment);
        } catch (PlaywrightException e) {
            logger.error("Timed out waiting for URL fragment: {}", urlFragment, e);
            throw e;
        }
    }

    public boolean isElementVisible(String selector) {
        try {
            return page.locator(selector).isVisible();
        } catch (PlaywrightException e) {
            logger.warn("Element not visible: {}", selector);
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Count
    // -------------------------------------------------------------------------

    public int countElements(String selector) {
        try {
            return page.locator(selector).count();
        } catch (PlaywrightException e) {
            logger.warn("Failed to count elements: {}", selector);
            return 0;
        }
    }
}
