package com.automation.assessment.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    
    static {
        loadProperties(DEFAULT_CONFIG_FILE);
    }

    /**
     * Load properties from a config file
     * 
     * @param configFile Path to the config file
     */
    private static void loadProperties(String configFile) {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                logger.error("Unable to find config file: " + configFile);
                return;
            }
            properties.load(input);
            logger.info("Successfully loaded properties from " + configFile);
        } catch (IOException e) {
            logger.error("Error loading properties file: " + e.getMessage());
        }
    }

    /**
     * Get a property value by key
     * 
     * @param key Property key
     * @return Property value, or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get a property value by key with a default value
     * 
     * @param key Property key
     * @param defaultValue Default value to return if key is not found
     * @return Property value, or default value if not found
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get the base URL for the web application
     * 
     * @return Base URL from the config file, or a default URL if not found
     */
    public static String getWebApplicationUrl() {
        return getProperty("web.application.url", "http://www.way2automation.com/angularjs-protractor/webtables/");
    }

    /**
     * Get the browser type to use
     * 
     * @return Browser type from the config file, or "chrome" if not found
     */
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    /**
     * Get the implicit wait time in seconds
     * 
     * @return Implicit wait time from the config file, or 10 if not found
     */
    public static int getImplicitWaitSeconds() {
        try {
            return Integer.parseInt(getProperty("implicit.wait.seconds", "10"));
        } catch (NumberFormatException e) {
            logger.error("Error parsing implicit wait seconds: " + e.getMessage());
            return 10;
        }
    }
    
    /**
     * Get the page load timeout in seconds
     * 
     * @return Page load timeout from the config file, or 30 if not found
     */
    public static int getPageLoadTimeoutSeconds() {
        try {
            return Integer.parseInt(getProperty("page.load.timeout.seconds", "30"));
        } catch (NumberFormatException e) {
            logger.error("Error parsing page load timeout seconds: " + e.getMessage());
            return 30;
        }
    }
    
    /**
     * Get the API base URL
     * 
     * @return API base URL from the config file, or default dog API URL if not found
     */
    public static String getApiBaseUrl() {
        return getProperty("api.base.url", "https://dog.ceo/api");
    }
    
    /**
     * Get the headless mode setting
     * 
     * @return true if headless mode is enabled in the config file, false otherwise
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
}
