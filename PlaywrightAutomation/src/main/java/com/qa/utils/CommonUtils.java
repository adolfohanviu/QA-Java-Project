package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

/**
 * CommonUtils provides common utility methods for test automation
 */
public class CommonUtils {
    private static final Logger logger = LogManager.getLogger(CommonUtils.class);

    /**
     * Pause/sleep for a specific milliseconds
     */
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted during pause", e);
        }
    }

    /**
     * Take a screenshot of the current page
     */
    public static String takeScreenshot(String fileName) {
        try {
            Page page = BrowserContextManager.getPage();
            String screenshotPath = "target/screenshots/" + fileName + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(screenshotPath)));
            logger.info("Screenshot taken: " + screenshotPath);
            return screenshotPath;
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }

    /**
     * Get current page title
     */
    public static String getPageTitle() {
        String title = BrowserContextManager.getPage().title();
        logger.info("Page title: " + title);
        return title;
    }

    /**
     * Get current page URL
     */
    public static String getCurrentURL() {
        String url = BrowserContextManager.getPage().url();
        logger.info("Current URL: " + url);
        return url;
    }

    /**
     * Generate random email for testing
     */
    public static String generateRandomEmail() {
        String email = "testuser" + System.currentTimeMillis() + "@test.com";
        logger.info("Generated random email: " + email);
        return email;
    }

    /**
     * Generate random string of specific length
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }
}
