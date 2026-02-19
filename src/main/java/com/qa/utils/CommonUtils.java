package com.qa.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

/**
 * CommonUtils — General-purpose test utilities.
 */
public final class CommonUtils {

    private static final Logger logger = LogManager.getLogger(CommonUtils.class);
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    private CommonUtils() {
        // Utility class — do not instantiate
    }

    // -------------------------------------------------------------------------
    // Browser helpers
    // -------------------------------------------------------------------------

    /**
     * Capture a screenshot of the current page.
     *
     * @param fileName Base filename (without extension)
     * @return Optional containing the screenshot path, or empty on failure
     */
    public static Optional<String> takeScreenshot(String fileName) {
        try {
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);

            Page page = BrowserContextManager.getPage();
            String screenshotPath = SCREENSHOT_DIR + fileName + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            logger.info("Screenshot captured: {}", screenshotPath);
            return Optional.of(screenshotPath);
        } catch (IOException | NullPointerException e) {
            logger.error("Failed to capture screenshot '{}'", fileName, e);
            return Optional.empty();
        }
    }

    /** @return The current page title. */
    public static String getPageTitle() {
        String title = BrowserContextManager.getPage().title();
        logger.info("Page title: {}", title);
        return title;
    }

    /** @return The current page URL. */
    public static String getCurrentURL() {
        String url = BrowserContextManager.getPage().url();
        logger.info("Current URL: {}", url);
        return url;
    }

    // -------------------------------------------------------------------------
    // Test data generation
    // -------------------------------------------------------------------------

    /**
     * Generate a unique email address suitable for test data.
     * Uses the current epoch millisecond to guarantee uniqueness within a run.
     *
     * @return A unique test email address
     */
    public static String generateRandomEmail() {
        String email = "testuser" + System.currentTimeMillis() + "@test.com";
        logger.info("Generated random email: {}", email);
        return email;
    }

    /**
     * Generate a random alphanumeric string of the given length.
     *
     * @param length Desired string length (must be > 0)
     * @return Random alphanumeric string
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive, got: " + length);
        }
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(rng.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Time helpers
    // -------------------------------------------------------------------------

    /**
     * Pause the current thread. Use sparingly — prefer Playwright's built-in
     * auto-wait (waitFor, waitForURL, waitForSelector) in almost all cases.
     *
     * @param milliseconds Duration to sleep
     */
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Thread interrupted during pause", e);
        }
    }
}
