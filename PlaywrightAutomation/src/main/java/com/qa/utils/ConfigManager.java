package com.qa.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ConfigManager loads and manages configuration properties
 * Supports multiple environment configurations
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
            config = ConfigFactory.load("application-" + env + ".conf");
            logger.info("Configuration loaded for environment: " + env);
        } catch (Exception e) {
            logger.warn("Environment config not found, loading default configuration");
            config = ConfigFactory.load("application.conf");
        }
    }

    // Base Configuration
    public static String getBaseURL() {
        return config.getString("base.url");
    }

    public static String getBrowserType() {
        return config.getString("browser.type");
    }

    public static boolean isHeadless() {
        return config.getBoolean("browser.headless");
    }

    public static int getTimeout() {
        return config.getInt("timeout.default");
    }

    public static int getWaitTimeout() {
        return config.getInt("timeout.wait");
    }

    // API Configuration
    public static String getAPIBaseURL() {
        return config.getString("api.base.url");
    }

    // Logging Configuration
    public static String getLogLevel() {
        return config.getString("log.level");
    }

    // Allure Configuration
    public static boolean isAllureEnabled() {
        return config.getBoolean("allure.enabled");
    }

    /**
     * Get custom configuration value
     */
    public static String getProperty(String key) {
        try {
            return config.getString(key);
        } catch (Exception e) {
            logger.warn("Property not found: " + key);
            return null;
        }
    }

    /**
     * Get integer configuration value
     */
    public static int getIntProperty(String key) {
        try {
            return config.getInt(key);
        } catch (Exception e) {
            logger.warn("Integer property not found: " + key);
            return 0;
        }
    }

    /**
     * Get boolean configuration value
     */
    public static boolean getBooleanProperty(String key) {
        try {
            return config.getBoolean(key);
        } catch (Exception e) {
            logger.warn("Boolean property not found: " + key);
            return false;
        }
    }
}
