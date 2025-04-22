package com.automation.assessment.stepdefinitions;

import com.automation.assessment.utils.APIUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class DogAPISteps {
    private static final Logger logger = LogManager.getLogger(DogAPISteps.class);
    private final APIUtils apiUtils = new APIUtils();
    private Response response;
    private List<String> subBreeds;

    @Given("I want to get a list of all dog breeds")
    public void iWantToGetAListOfAllDogBreeds() {
        logger.info("Setting up to get all dog breeds");
    }

    @When("I make a request to get all dog breeds")
    public void iMakeARequestToGetAllDogBreeds() {
        logger.info("Making request to get all dog breeds");
        response = apiUtils.getAllBreeds();
        logger.info("Response received: " + response.asString());
    }

    @Then("the API call should be successful with status code {int}")
    public void theAPICallShouldBeSuccessfulWithStatusCode(int expectedStatusCode) {
        logger.info("Validating status code: " + response.getStatusCode());
        Assert.assertEquals("Status code is not as expected", expectedStatusCode, response.getStatusCode());
    }

    @And("the response should contain a list of breeds")
    public void theResponseShouldContainAListOfBreeds() {
        logger.info("Validating response contains breeds list");
        Map<String, Object> jsonResponse = response.jsonPath().getMap("message");
        Assert.assertNotNull("Message field is not found in response", jsonResponse);
        Assert.assertFalse("Breeds list is empty", jsonResponse.isEmpty());
        logger.info("Found " + jsonResponse.size() + " breeds in the response");
    }

    @And("the list should contain {string} breed")
    public void theListShouldContainBreed(String breedName) {
        logger.info("Validating response contains breed: " + breedName);
        Map<String, Object> jsonResponse = response.jsonPath().getMap("message");
        Assert.assertTrue("Breed '" + breedName + "' is not found in the list", jsonResponse.containsKey(breedName));
        logger.info("Successfully validated breed '" + breedName + "' exists in the list");
    }

    @Given("I want to get a list of sub-breeds for {string}")
    public void iWantToGetAListOfSubBreedsFor(String breedName) {
        logger.info("Setting up to get sub-breeds for: " + breedName);
    }

    @When("I make a request to get sub-breeds for {string}")
    public void iMakeARequestToGetSubBreedsFor(String breedName) {
        logger.info("Making request to get sub-breeds for: " + breedName);
        response = apiUtils.getSubBreeds(breedName);
        logger.info("Response received: " + response.asString());
    }

    @And("the response should contain a list of sub-breeds")
    public void theResponseShouldContainAListOfSubBreeds() {
        logger.info("Validating response contains sub-breeds list");
        List<String> subBreedsList = response.jsonPath().getList("message");
        Assert.assertNotNull("Message field is not found in response", subBreedsList);
        Assert.assertFalse("Sub-breeds list is empty", subBreedsList.isEmpty());
        logger.info("Found " + subBreedsList.size() + " sub-breeds in the response");
    }

    @And("I should store the list of sub-breeds for validation")
    public void iShouldStoreTheListOfSubBreedsForValidation() {
        logger.info("Storing sub-breeds for later validation");
        subBreeds = response.jsonPath().getList("message");
        logger.info("Stored sub-breeds: " + subBreeds);
    }

    @Given("I want to get a random image for {string} sub-breed of {string}")
    public void iWantToGetARandomImageForSubBreedOf(String subBreed, String breed) {
        logger.info("Setting up to get random image for " + subBreed + " sub-breed of " + breed);
    }

    @When("I make a request to get a random image for {string} sub-breed of {string}")
    public void iMakeARequestToGetARandomImageForSubBreedOf(String subBreed, String breed) {
        logger.info("Making request to get random image for " + subBreed + " sub-breed of " + breed);
        response = apiUtils.getRandomImageBySubBreed(breed, subBreed);
        logger.info("Response received: " + response.asString());
    }

    @And("the response should contain a random image URL")
    public void theResponseShouldContainARandomImageURL() {
        logger.info("Validating response contains random image URL");
        String imageUrl = response.jsonPath().getString("message");
        Assert.assertNotNull("Message field is not found in response", imageUrl);
        Assert.assertFalse("Image URL is empty", imageUrl.isEmpty());
        logger.info("Image URL found: " + imageUrl);
    }

    @And("the image URL should be valid")
    public void theImageURLShouldBeValid() {
        logger.info("Validating image URL is valid");
        String imageUrl = response.jsonPath().getString("message");
        // Validate URL format using regex for a basic URL pattern
        Assert.assertTrue("Image URL is not valid", 
                imageUrl.matches("^https?://.*\\.(jpg|jpeg|png|gif)$"));
        
        // Optionally, verify the image is accessible
        Response imageResponse = apiUtils.makeGetRequest(imageUrl);
        Assert.assertEquals("Image URL is not accessible", 200, imageResponse.getStatusCode());
        logger.info("Successfully validated image URL is accessible: " + imageUrl);
    }
}
