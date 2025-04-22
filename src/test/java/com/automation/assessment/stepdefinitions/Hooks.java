package com.automation.assessment.stepdefinitions;

import com.automation.assessment.utils.WebDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private WebDriver driver;

    @Before(value = "@web")
    public void setupWeb() {
        logger.info("Starting Web scenario");
        driver = WebDriverManager.getDriver();
    }

    @Before(value = "@api")
    public void setupAPI() {
        logger.info("Starting API scenario");
    }

    @AfterStep(value = "@web")
    public void captureScreenshotAfterStep(Scenario scenario) {
        if (driver != null) {
            // Take screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            
            // Also save to disk for reference
            try {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String filePath = "test-output/screenshots/" + scenario.getName().replaceAll("\\s+", "_") + 
                                  "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
                
                // Ensure directory exists
                Files.createDirectories(Paths.get("test-output/screenshots/"));
                
                FileUtils.copyFile(screenshotFile, new File(filePath));
                logger.info("Screenshot saved to: " + filePath);
            } catch (IOException e) {
                logger.error("Failed to save screenshot: " + e.getMessage());
            }
        }
    }

    @After(value = "@web")
    public void tearDownWeb(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            // Take screenshot on failure
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "FailureScreenshot");
            logger.info("Scenario failed, screenshot attached");
        }
        
        if (driver != null) {
            WebDriverManager.quitDriver();
            logger.info("Web driver quit successfully");
        }
    }

    @After(value = "@api")
    public void tearDownAPI() {
        logger.info("API scenario completed");
    }
}
