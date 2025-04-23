package com.automation.assessment.pageobjects;

import com.automation.assessment.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebTablePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WebTablePage.class);
    private static final String PAGE_URL = ConfigReader.getWebApplicationUrl();

    // Page title
    @FindBy(css = "h4.modal-title")
    private WebElement pageTitle;

    // Table elements
    @FindBy(css = "table.smart-table")
    private WebElement userTable;

    @FindBy(css = "table.smart-table tbody tr")
    private List<WebElement> tableRows;

    // Add User button
    @FindBy(xpath=("//h3[text()='Add User']")
    private WebElement addUserButton;

    // Add User form elements
    @FindBy(css = "input[name='FirstName']")
    private WebElement firstNameInput;

    @FindBy(css = "input[name='LastName']")
    private WebElement lastNameInput;

    @FindBy(css = "input[name='UserName']")
    private WebElement userNameInput;

    @FindBy(css = "input[name='Password']")
    private WebElement passwordInput;

    @FindBy(css = "input[name='Email']")
    private WebElement emailInput;

    @FindBy(css = "input[name='Mobilephone']")
    private WebElement cellPhoneInput;

    @FindBy(css = "select[name='RoleId']")
    private WebElement roleSelect;

    @FindBy(xpath = "//label[contains(text(), 'Customer')]/../input[@type='radio']")
    private List<WebElement> customerRadioButtons;

    @FindBy(css = "button.btn-success")
    private WebElement saveButton;

    public WebTablePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to the web table page
     */
    public void navigateToPage() {
        driver.get(PAGE_URL);
        waitForVisibility(userTable);
        logger.info("Navigated to Web Table page: " + PAGE_URL);
    }

    /**
     * Check if the user list table is displayed
     *
     * @return true if the user list table is displayed, false otherwise
     */
    public boolean isUserListTableDisplayed() {
        return isDisplayed(userTable);
    }

    /**
     * Click on the Add User button
     */
    public void clickAddUserButton() {
        click(addUserButton);
        // Wait for modal title to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(addUserButton));
        logger.info("Clicked Add User button");
    }

    /**
     * Enter user details in the Add User form
     *
     * @param firstName First name
     * @param lastName  Last name
     * @param userName  User name
     * @param password  Password
     * @param customer  Customer selection (Company A, Company B, etc.)
     * @param role      Role selection (Admin, Customer, etc.)
     * @param email     Email address
     * @param cellPhone Cell phone number
     */
    public void enterUserDetails(String firstName, String lastName, String userName,
                                 String password, String customer, String role,
                                 String email, String cellPhone) {
        logger.info("Entering user details: " + firstName + " " + lastName);

        // Fill in text fields
        sendKeys(firstNameInput, firstName);
        sendKeys(lastNameInput, lastName);
        sendKeys(userNameInput, userName);
        sendKeys(passwordInput, password);
        sendKeys(emailInput, email);
        sendKeys(cellPhoneInput, cellPhone);

        // Select role
        selectByVisibleText(roleSelect, role);

        // Select customer radio button
        selectCustomer(customer);

        logger.info("User details entered successfully");
    }

    /**
     * Click on the Save button
     */
    public void clickSaveButton() {
        click(saveButton);
        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(addUserButton);
        logger.info("Clicked Save button");
    }
     /**
     * Generate unique username based on timestamp and random characters
     * @return Unique username
     */
    public static String generateUniqueUsername() {
        // Create a shorter, more readable unique username
        long timestamp = System.currentTimeMillis() % 10000; // Last 4 digits of timestamp
        String randomChars = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        String username = "test_user_" + timestamp + "_" + randomChars;
        
        // Print the generated username for debugging
        System.out.println("Generated unique username: " + username);
        
        return username;
    }

    /**
     * Select customer radio button based on customer name
     *
     * @param customerName Customer name (Company A, Company B, etc.)
     */
    private void selectCustomer(String customerName) {
        boolean found = false;

        // Using dynamic locator with text to find the right radio button
        try {
            WebElement radioButton = driver.findElement(
                    By.xpath("//label[contains(text(), '" + customerName + "')]/../input[@type='radio']"));
            click(radioButton);
            found = true;
            logger.info("Selected customer: " + customerName);
        } catch (Exception e) {
            logger.warn("Could not find radio button for customer: " + customerName);
        }

        // Fallback: try each radio button in the list
        if (!found && !customerRadioButtons.isEmpty()) {
            click(customerRadioButtons.get(0));
            logger.info("Selected first customer as fallback");
        }
    }

    /**
     * Check if a user exists in the table
     *
     * @param firstName First name
     * @param lastName  Last name
     * @return true if the user exists, false otherwise
     */
    public boolean isUserInTable(String firstName, String lastName) {
        // Wait for table to refresh
        wait.until(ExpectedConditions.visibilityOf(userTable));

        // Create XPath to look for the user
        String userXPath = "//table[contains(@class,'smart-table')]//tr[td[contains(text(),'" +
                firstName + "')] and td[contains(text(),'" + lastName + "')]]";

        try {
            WebElement userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(userXPath)));
            logger.info("User found in table: " + firstName + " " + lastName);
            return true;
        } catch (Exception e) {
            logger.warn("User not found in table: " + firstName + " " + lastName);
            return false;
        }
    }

    /**
     * Get user details from the table
     *
     * @param firstName First name
     * @param lastName  Last name
     * @return Map containing all user details from the table
     */
    public Map<String, String> getUserDetailsFromTable(String firstName, String lastName) {
        Map<String, String> userDetails = new HashMap<>();

        // Create XPath to look for the user
        String userXPath = "//table[contains(@class,'smart-table')]//tr[td[contains(text(),'" +
                firstName + "')] and td[contains(text(),'" + lastName + "')]]";

        try {
            WebElement userRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(userXPath)));
            List<WebElement> cells = userRow.findElements(By.tagName("td"));

            // Get column headers
            List<WebElement> headers = driver.findElements(By.cssSelector("table.smart-table th"));

            // Map cell values to headers
            for (int i = 0; i < headers.size() && i < cells.size(); i++) {
                String header = headers.get(i).getText();
                String value = cells.get(i).getText();
                userDetails.put(header, value);
            }

            // Additional mapping for specific fields
            if (userDetails.containsKey("#")) userDetails.put("ID", userDetails.get("#"));
            if (userDetails.containsKey("First Name")) userDetails.put("FirstName", userDetails.get("First Name"));
            if (userDetails.containsKey("Last Name")) userDetails.put("LastName", userDetails.get("Last Name"));
            if (userDetails.containsKey("User Name")) userDetails.put("UserName", userDetails.get("User Name"));

            logger.info("Retrieved user details from table: " + userDetails);

        } catch (Exception e) {
            logger.error("Failed to get user details from table: " + e.getMessage());
        }

        return userDetails;
    }
}
