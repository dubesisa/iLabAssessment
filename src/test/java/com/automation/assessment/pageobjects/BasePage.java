package com.automation.assessment.pageobjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    private static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be clickable and click it
     * 
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            logger.info("Clicked on element: " + getElementDescription(element));
        } catch (Exception e) {
            logger.error("Failed to click on element: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for element to be visible and click it by locator
     * 
     * @param locator By locator to find the element
     */
    protected void click(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Clicked on element by locator: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click on element by locator " + locator + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for element to be visible and enter text
     * 
     * @param element WebElement to enter text into
     * @param text Text to enter
     */
    protected void sendKeys(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '" + text + "' into element: " + getElementDescription(element));
        } catch (Exception e) {
            logger.error("Failed to enter text into element: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for element to be visible and enter text by locator
     * 
     * @param locator By locator to find the element
     * @param text Text to enter
     */
    protected void sendKeys(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '" + text + "' into element by locator: " + locator);
        } catch (Exception e) {
            logger.error("Failed to enter text into element by locator " + locator + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Select option from dropdown by visible text
     * 
     * @param element Select WebElement
     * @param visibleText Text of the option to select
     */
    protected void selectByVisibleText(WebElement element, String visibleText) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            Select select = new Select(element);
            select.selectByVisibleText(visibleText);
            logger.info("Selected option '" + visibleText + "' from dropdown: " + getElementDescription(element));
        } catch (Exception e) {
            logger.error("Failed to select option from dropdown: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for element to be visible
     * 
     * @param element WebElement to wait for
     * @return The WebElement once it's visible
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be visible by locator
     * 
     * @param locator By locator to find the element
     * @return The WebElement once it's visible
     */
    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Check if element is displayed
     * 
     * @param element WebElement to check
     * @return true if element is displayed, false otherwise
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Check if element is displayed by locator
     * 
     * @param locator By locator to find the element
     * @return true if element is displayed, false otherwise
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Get text from element
     * 
     * @param element WebElement to get text from
     * @return Text of the element
     */
    protected String getText(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getText();
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + e.getMessage());
            return "";
        }
    }

    /**
     * Get text from element by locator
     * 
     * @param locator By locator to find the element
     * @return Text of the element
     */
    protected String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText();
        } catch (Exception e) {
            logger.error("Failed to get text from element by locator " + locator + ": " + e.getMessage());
            return "";
        }
    }

    /**
     * Get element description for logging
     * 
     * @param element WebElement
     * @return Description of the element
     */
    private String getElementDescription(WebElement element) {
        try {
            String id = element.getAttribute("id");
            String name = element.getAttribute("name");
            String className = element.getAttribute("class");
            String tag = element.getTagName();
            
            StringBuilder description = new StringBuilder(tag);
            
            if (id != null && !id.isEmpty()) {
                description.append("[id=").append(id).append("]");
            } else if (name != null && !name.isEmpty()) {
                description.append("[name=").append(name).append("]");
            } else if (className != null && !className.isEmpty()) {
                description.append("[class=").append(className).append("]");
            }
            
            return description.toString();
        } catch (Exception e) {
            return "Unknown Element";
        }
    }

    /**
     * Scroll element into view
     * 
     * @param element WebElement to scroll to
     */
    protected void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled element into view: " + getElementDescription(element));
        } catch (Exception e) {
            logger.error("Failed to scroll element into view: " + e.getMessage());
        }
    }

    /**
     * Execute JavaScript
     * 
     * @param script JavaScript to execute
     * @param args Arguments for the script
     * @return Result of the script execution
     */
    protected Object executeJavaScript(String script, Object... args) {
        try {
            return ((JavascriptExecutor) driver).executeScript(script, args);
        } catch (Exception e) {
            logger.error("Failed to execute JavaScript: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if element exists
     * 
     * @param locator By locator to find the element
     * @return true if element exists, false otherwise
     */
    protected boolean exists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
