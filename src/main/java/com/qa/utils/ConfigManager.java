package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

/**
 * ConfigManager loads and manages configuration properties
 * Supports multiple environment configurations with proper null handling
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Config config;

    static {
        loadConfig();
    }

    /**
     * Load configuration based on environment
     */
    private static void loadConfig() {
        try {
            String env = System.getenv("TEST_ENV") != null ? System.getenv("TEST_ENV") : "dev";
            config = ConfigFactory.parseResources("application-" + env + ".conf")
                    .withFallback(ConfigFactory.load("application.conf"))
                    .resolve();
            logger.info(String.format("Configuration loaded for environment: %s", env));
        } catch (ConfigException.IO e) {
            logger.warn("Environment config not found, loading default configuration", e);
            config = ConfigFactory.load("application.conf");
        } catch (ConfigException e) {
            logger.error("Failed to parse configuration", e);
            throw new RuntimeException("Configuration loading failed", e);
        }
    }

    /**
     * Get base URL for testing
     * @return Base URL
     * @throws RuntimeException if base.url is not configured
     */
    public static String getBaseURL() {
        return getStringConfig("base.url", "Base URL not configured");
    }

    /**
     * Get browser type to use for tests
     * @return Browser type (chromium, firefox, webkit)
     */
    public static String getBrowserType() {
        return getStringConfig("browser.type", "chromium");
    }

    /**
     * Check if browser should run in headless mode
     * @return true if headless, false otherwise
     */
    public static boolean isHeadless() {
        String headlessOverride = System.getProperty("browser.headless");
        if (headlessOverride == null || headlessOverride.isBlank()) {
            headlessOverride = System.getProperty("HEADLESS");
        }
        if (headlessOverride == null || headlessOverride.isBlank()) {
            headlessOverride = System.getenv("HEADLESS");
        }

        if (headlessOverride != null && !headlessOverride.isBlank()) {
            return Boolean.parseBoolean(headlessOverride.trim());
        }

        try {
            return config.getBoolean("browser.headless");
        } catch (ConfigException.WrongType e) {
            String configValue = config.getString("browser.headless");
            logger.warn(String.format("browser.headless is configured as string, converting: %s", configValue));
            return Boolean.parseBoolean(configValue);
        } catch (ConfigException e) {
            logger.warn("browser.headless not configured, defaulting to true");
            return true;
        }
    }

    /**
     * Get default timeout in milliseconds
     * @return Timeout value
     */
    public static int getTimeout() {
        return getIntConfig("timeout.default", 30000);
    }

    /**
     * Get wait timeout in milliseconds
     * @return Wait timeout value
     */
    public static int getWaitTimeout() {
        return getIntConfig("timeout.wait", 10000);
    }

    /**
     * Get API base URL
     * @return API base URL
     */
    public static String getAPIBaseURL() {
        return getStringConfig("api.base.url", "API base URL not configured");
    }

    /**
     * Get log level
     * @return Log level (DEBUG, INFO, WARN, ERROR)
     */
    public static String getLogLevel() {
        return getStringConfig("log.level", "INFO");
    }

    /**
     * Check if Allure reporting is enabled
     * @return true if enabled, false otherwise
     */
    public static boolean isAllureEnabled() {
        return getBooleanConfig("allure.enabled", true);
    }

    /**
     * Get custom string configuration value
     * @param key Configuration key
     * @return Configuration value or empty string if not found
     */
    public static String getProperty(String key) {
        return getStringConfig(key, "");
    }

    /**
     * Get custom string configuration value with default
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return getStringConfig(key, defaultValue);
    }

    /**
     * Get integer configuration value
     * @param key Configuration key
     * @return Configuration value or 0 if not found
     */
    public static int getIntProperty(String key) {
        return getIntConfig(key, 0);
    }

    /**
     * Get integer configuration value with default
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    public static int getIntProperty(String key, int defaultValue) {
        return getIntConfig(key, defaultValue);
    }

    /**
     * Get boolean configuration value
     * @param key Configuration key
     * @return Configuration value or false if not found
     */
    public static boolean getBooleanProperty(String key) {
        return getBooleanConfig(key, false);
    }

    /**
     * Get boolean configuration value with default
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return getBooleanConfig(key, defaultValue);
    }

    // Helper methods

    /**
     * Safely get string configuration with default value
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    private static String getStringConfig(String key, String defaultValue) {
        try {
            String value = config.getString(key);
            if (value == null || value.isBlank()) {
                logger.debug(String.format("Configuration key '%s' is empty, using default", key));
                return defaultValue;
            }
            return value;
        } catch (ConfigException.Missing e) {
            logger.warn(String.format("Configuration key not found: %s", key));
            return defaultValue;
        } catch (ConfigException e) {
            logger.error(String.format("Error reading configuration key: %s", key), e);
            return defaultValue;
        }
    }

    /**
     * Safely get integer configuration with default value
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    private static int getIntConfig(String key, int defaultValue) {
        try {
            return config.getInt(key);
        } catch (ConfigException.Missing e) {
            logger.debug(String.format("Integer configuration key not found: %s, using default: %d", key, defaultValue));
            return defaultValue;
        } catch (ConfigException.WrongType e) {
            logger.warn(String.format("Configuration key '%s' is not an integer, using default: %d", key, defaultValue));
            return defaultValue;
        } catch (ConfigException e) {
            logger.error(String.format("Error reading integer configuration key: %s", key), e);
            return defaultValue;
        }
    }

    /**
     * Safely get boolean configuration with default value
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value or default
     */
    private static boolean getBooleanConfig(String key, boolean defaultValue) {
        try {
            return config.getBoolean(key);
        } catch (ConfigException.WrongType e) {
            try {
                String value = config.getString(key);
                logger.debug(String.format("Boolean property '%s' is configured as string, converting: %s", key, value));
                return Boolean.parseBoolean(value);
            } catch (ConfigException ex) {
                logger.warn(String.format("Could not convert boolean property: %s, using default: %b", key, defaultValue));
                return defaultValue;
            }
        } catch (ConfigException.Missing e) {
            logger.debug(String.format("Boolean configuration key not found: %s, using default: %b", key, defaultValue));
            return defaultValue;
        } catch (ConfigException e) {
            logger.error(String.format("Error reading boolean configuration key: %s", key), e);
            return defaultValue;
        }
    }
}