package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

/**
 * ConfigManager loads and provides typed access to configuration properties.
 *
 * Supports multiple environments via the TEST_ENV environment variable.
 * Falls back to application.conf when an environment-specific file is absent.
 *
 */
public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Config config;

    static {
        loadConfig();
    }

    private ConfigManager() {
        // Utility class — do not instantiate
    }

    // -------------------------------------------------------------------------
    // Config loading
    // -------------------------------------------------------------------------

    private static void loadConfig() {
        try {
            String env = System.getenv("TEST_ENV");
            if (env == null || env.isBlank()) {
                env = "dev";
            }
            // Layer: environment-specific file → default application.conf
            config = ConfigFactory.parseResources("application-" + env + ".conf")
                    .withFallback(ConfigFactory.load("application.conf"))
                    .resolve();
            logger.info("Configuration loaded for environment: {}", env);
        } catch (ConfigException.IO e) {
            logger.warn("Environment-specific config not found; loading default application.conf", e);
            config = ConfigFactory.load("application.conf");
        } catch (ConfigException e) {
            logger.error("Failed to parse configuration", e);
            throw new RuntimeException("Configuration loading failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Public accessors
    // -------------------------------------------------------------------------

    /** @return Base URL for the application under test */
    public static String getBaseURL() {
        return getStringConfig("base.url", "https://www.saucedemo.com/");
    }

    /** @return Browser type: chromium | firefox | webkit */
    public static String getBrowserType() {
        return getStringConfig("browser.type", "chromium");
    }

    /**
     * Headless mode: checks system property → environment variable → config file.
     *
     * @return true if headless, false for headed mode
     */
    public static boolean isHeadless() {
        // System property takes highest precedence (e.g., -Dbrowser.headless=false)
        for (String key : new String[]{"browser.headless", "HEADLESS"}) {
            String val = System.getProperty(key);
            if (val != null && !val.isBlank()) {
                return Boolean.parseBoolean(val.trim());
            }
        }
        // Then environment variable
        String envVal = System.getenv("HEADLESS");
        if (envVal != null && !envVal.isBlank()) {
            return Boolean.parseBoolean(envVal.trim());
        }
        return getBooleanConfig("browser.headless", true);
    }

    /** @return Default element/page timeout in milliseconds */
    public static int getTimeout() {
        return getIntConfig("timeout.default", 30000);
    }

    /** @return Wait timeout for explicit waits in milliseconds */
    public static int getWaitTimeout() {
        return getIntConfig("timeout.wait", 10000);
    }

    /** @return Base URL for the API under test */
    public static String getAPIBaseURL() {
        return getStringConfig("api.base.url", "https://jsonplaceholder.typicode.com");
    }

    /** @return API request timeout in milliseconds */
    public static int getAPITimeout() {
        return getIntConfig("api.timeout", 10000);
    }

    /** @return Log level string (DEBUG | INFO | WARN | ERROR) */
    public static String getLogLevel() {
        return getStringConfig("log.level", "INFO");
    }

    /** @return true if Allure reporting is enabled */
    public static boolean isAllureEnabled() {
        return getBooleanConfig("allure.enabled", true);
    }

    /**
     * Get a custom string property.
     *
     * @param key Configuration key
     * @return Value or empty string
     */
    public static String getProperty(String key) {
        return getStringConfig(key, "");
    }

    /**
     * Get a custom string property with a caller-supplied default.
     *
     * @param key          Configuration key
     * @param defaultValue Fallback value
     * @return Value or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        return getStringConfig(key, defaultValue);
    }

    /**
     * Get a custom integer property.
     *
     * @param key Configuration key
     * @return Value or 0
     */
    public static int getIntProperty(String key) {
        return getIntConfig(key, 0);
    }

    /**
     * Get a custom boolean property.
     *
     * @param key Configuration key
     * @return Value or false
     */
    public static boolean getBooleanProperty(String key) {
        return getBooleanConfig(key, false);
    }

    // -------------------------------------------------------------------------
    // Private helpers with safe defaults
    // -------------------------------------------------------------------------

    private static String getStringConfig(String key, String defaultValue) {
        try {
            return config.getString(key);
        } catch (ConfigException.Missing e) {
            logger.debug("Config key '{}' not found; using default: '{}'", key, defaultValue);
            return defaultValue;
        } catch (ConfigException e) {
            logger.warn("Error reading config key '{}'; using default: '{}'", key, defaultValue);
            return defaultValue;
        }
    }

    private static int getIntConfig(String key, int defaultValue) {
        try {
            return config.getInt(key);
        } catch (ConfigException.Missing e) {
            logger.debug("Config key '{}' not found; using default: {}", key, defaultValue);
            return defaultValue;
        } catch (ConfigException e) {
            logger.warn("Error reading int config key '{}'; using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    private static boolean getBooleanConfig(String key, boolean defaultValue) {
        try {
            return config.getBoolean(key);
        } catch (ConfigException.Missing e) {
            logger.debug("Config key '{}' not found; using default: {}", key, defaultValue);
            return defaultValue;
        } catch (ConfigException e) {
            // Gracefully handle config value stored as a string (e.g. "true" instead of true)
            String raw = getStringConfig(key, String.valueOf(defaultValue));
            logger.warn("Config key '{}' error; converting '{}' to boolean", key, raw);
            return Boolean.parseBoolean(raw);
        }
    }
}
