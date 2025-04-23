package com.automation.assessment.stepdefinitions;

import com.automation.assessment.pageobjects.WebTablePage;
import com.automation.assessment.utils.WebDriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class WebTableSteps {
    private static final Logger logger = LogManager.getLogger(WebTableSteps.class);
    private final WebDriver driver;
    private final WebTablePage webTablePage;

    private Map<String, String> lastAddedUser;

    public WebTableSteps() {
        driver = WebDriverManager.getDriver();
        webTablePage = new WebTablePage(driver);
    }

    @Given("I navigate to the web table application")
    public void iNavigateToTheWebTableApplication() {
        logger.info("Navigating to web table application");
        webTablePage.navigateToPage();
    }

    @And("I am on the User List Table page")
    public void iAmOnTheUserListTablePage() {
        logger.info("Verifying User List Table page is displayed");
        Assert.assertTrue("User List Table page is not displayed", webTablePage.isUserListTableDisplayed());
    }

    @When("I click on Add User button")
    public void iClickOnAddUserButton() {
        logger.info("Clicking on Add User button");
        webTablePage.clickAddUserButton();
    }

    @And("I enter user details:")
    public void iEnterUserDetails(DataTable dataTable) {
        List<Map<String, String>> userDetails = dataTable.asMaps(String.class, String.class);
        Map<String, String> user = userDetails.get(0);

        // Replace timestamp placeholder in username if present
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String userName = user.get("UserName").replace("{TS}", timestamp);
        user.put("UserName", userName);

        logger.info("Entering user details: " + user);
        webTablePage.enterUserDetails(
                user.get("FirstName"),
                user.get("LastName"),
                user.get("UserName"),
                user.get("Password"),
                user.get("Customer"),
                user.get("Role"),
                user.get("Email"),
                user.get("CellPhone")
        );

        // Store for later validation
        lastAddedUser = user;
    }

    @And("I click on Save button")
    public void iClickOnSaveButton() {
        logger.info("Clicking on Save button");
        webTablePage.clickSaveButton();
    }

    @Then("the user should be added to the User List Table")
    public void theUserShouldBeAddedToTheUserListTable() {
        logger.info("Verifying user is added to the table");
        Assert.assertTrue("User not found in table",
                webTablePage.isUserInTable(lastAddedUser.get("FirstName"), lastAddedUser.get("LastName")));
    }

    @And("the user details should match the entered data")
    public void theUserDetailsShouldMatchTheEnteredData() {
        logger.info("Verifying user details match entered data");
        Map<String, String> userDetailsFromTable = webTablePage.getUserDetailsFromTable(
                lastAddedUser.get("FirstName"), lastAddedUser.get("LastName"));

        Assert.assertEquals("First name doesn't match",
                lastAddedUser.get("FirstName"), userDetailsFromTable.get("FirstName"));
        Assert.assertEquals("Last name doesn't match",
                lastAddedUser.get("LastName"), userDetailsFromTable.get("LastName"));
        Assert.assertEquals("User name doesn't match",
                lastAddedUser.get("UserName"), userDetailsFromTable.get("UserName"));
        // We can't verify password as it's masked/not displayed in the table
        Assert.assertEquals("Customer doesn't match",
                lastAddedUser.get("Customer"), userDetailsFromTable.get("Customer"));
        Assert.assertEquals("Role doesn't match",
                lastAddedUser.get("Role"), userDetailsFromTable.get("Role"));
        Assert.assertEquals("Email doesn't match",
                lastAddedUser.get("Email"), userDetailsFromTable.get("Email"));
        Assert.assertEquals("Cell phone doesn't match",
                lastAddedUser.get("CellPhone"), userDetailsFromTable.get("CellPhone"));

        logger.info("User details successfully verified");
    }

  
}
