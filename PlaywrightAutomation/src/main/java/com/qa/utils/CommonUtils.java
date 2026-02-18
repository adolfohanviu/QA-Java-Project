package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

/**
 * CommonUtils provides common utility methods for test automation
 * Includes screenshot capture, random data generation, and page utilities
 */
public class CommonUtils {
    private static final Logger logger = LogManager.getLogger(CommonUtils.class);

    /**
     * Pause/sleep for a specific duration
     * Use sparingly - prefer Playwright's built-in wait mechanisms instead
     * 
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            logger.warn(String.format("Thread interrupted during pause of %d ms", milliseconds), e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Take a screenshot of the current page
     * Screenshots are saved to target/screenshots/ directory
     * 
     * @param fileName Screenshot file name (without extension, .png will be added)
     * @return Path to saved screenshot file, or null if screenshot failed
     */
    public static String takeScreenshot(String fileName) {
        try {
            Page page = BrowserContextManager.getPage();
            String screenshotPath = String.format("target/screenshots/%s.png", fileName);
            java.nio.file.Path screenshotDir = java.nio.file.Paths.get("target/screenshots");
            java.nio.file.Files.createDirectories(screenshotDir);
            page.screenshot(new Page.ScreenshotOptions().setPath(java.nio.file.Paths.get(screenshotPath)));
            logger.info(String.format("Screenshot taken: %s", screenshotPath));
            return screenshotPath;
        } catch (Exception e) {
            logger.error(String.format("Failed to take screenshot: %s", fileName), e);
            return null;
        }
    }

    /**
     * Get current page title
     * 
     * @return Page title text
     */
    public static String getPageTitle() {
        String title = BrowserContextManager.getPage().title();
        logger.info(String.format("Page title: %s", title));
        return title;
    }

    /**
     * Get current page URL
     * 
     * @return Current page URL
     */
    public static String getCurrentURL() {
        String url = BrowserContextManager.getPage().url();
        logger.info(String.format("Current URL: %s", url));
        return url;
    }

    /**
     * Generate random email address for test data
     * Format: testuser[timestamp]@test.com
     * Useful for scenarios requiring unique email addresses
     * 
     * @return Email address string with current timestamp
     */
    public static String generateRandomEmail() {
        String email = String.format("testuser%d@test.com", System.currentTimeMillis());
        logger.info(String.format("Generated random email: %s", email));
        return email;
    }

    /**
     * Generate random alphanumeric string of specified length
     * Characters: A-Z and 0-9
     * 
     * @param length Number of characters to generate
     * @return Random string of specified length
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            logger.warn("Requested string length must be greater than 0, returning empty string");
            return "";
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        
        String result = sb.toString();
        logger.info(String.format("Generated random string of length %d", length));
        return result;
    }
}