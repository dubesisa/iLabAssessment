package com.automation.assessment.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class WebDriverManager {
    private static final Logger logger = LogManager.getLogger(WebDriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Get the WebDriver instance for the current thread.
     * If none exists, a new one will be created.
     *
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initializeDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Initialize a new WebDriver instance based on configuration
     */
    private static void initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();
        int implicitWaitSeconds = ConfigReader.getImplicitWaitSeconds();
        int pageLoadTimeoutSeconds = ConfigReader.getPageLoadTimeoutSeconds();

        WebDriver driver;

        logger.info("Initializing WebDriver for browser: " + browser + " (headless: " + headless + ")");

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "safari":
                driver = new SafariDriver();
                break;

            default:
                logger.warn("Unsupported browser: " + browser + ". Using Chrome as default.");
                driver = new ChromeDriver();
        }

        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeoutSeconds));
        driver.manage().window().maximize();

        // Store in ThreadLocal
        driverThreadLocal.set(driver);
        logger.info("WebDriver initialized successfully");
    }

    /**
     * Quit the WebDriver instance for the current thread
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting WebDriver");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
