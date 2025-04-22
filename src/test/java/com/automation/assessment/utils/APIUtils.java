package com.automation.assessment.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class APIUtils {
    private static final Logger logger = LogManager.getLogger(APIUtils.class);
    private static final String BASE_URL = "https://dog.ceo/api";

    /**
     * Make a GET request to the specified URL
     * 
     * @param url URL to make the GET request to
     * @return Response object containing the response details
     */
    public Response makeGetRequest(String url) {
        logger.info("Making GET request to: " + url);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get(url);
    }

    /**
     * Make a GET request with query parameters
     * 
     * @param url URL to make the GET request to
     * @param queryParams Map of query parameters
     * @return Response object containing the response details
     */
    public Response makeGetRequestWithParams(String url, Map<String, String> queryParams) {
        logger.info("Making GET request to: " + url + " with params: " + queryParams);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParams(queryParams)
                .when()
                .get(url);
    }

    /**
     * Make a POST request with a JSON body
     * 
     * @param url URL to make the POST request to
     * @param body JSON body to include in the request
     * @return Response object containing the response details
     */
    public Response makePostRequest(String url, Object body) {
        logger.info("Making POST request to: " + url);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url);
    }

    /**
     * Get all dog breeds
     * 
     * @return Response object containing all dog breeds
     */
    public Response getAllBreeds() {
        String url = BASE_URL + "/breeds/list/all";
        logger.info("Getting all dog breeds from: " + url);
        return makeGetRequest(url);
    }

    /**
     * Get all sub-breeds for a specific breed
     * 
     * @param breed Dog breed to get sub-breeds for
     * @return Response object containing all sub-breeds for the specified breed
     */
    public Response getSubBreeds(String breed) {
        String url = BASE_URL + "/breed/" + breed + "/list";
        logger.info("Getting sub-breeds for " + breed + " from: " + url);
        return makeGetRequest(url);
    }

    /**
     * Get a random image for a specific breed
     * 
     * @param breed Dog breed to get random image for
     * @return Response object containing a random image URL for the specified breed
     */
    public Response getRandomImageByBreed(String breed) {
        String url = BASE_URL + "/breed/" + breed + "/images/random";
        logger.info("Getting random image for " + breed + " from: " + url);
        return makeGetRequest(url);
    }

    /**
     * Get a random image for a specific sub-breed
     * 
     * @param breed Main dog breed
     * @param subBreed Sub-breed to get random image for
     * @return Response object containing a random image URL for the specified sub-breed
     */
    public Response getRandomImageBySubBreed(String breed, String subBreed) {
        String url = BASE_URL + "/breed/" + breed + "/" + subBreed + "/images/random";
        logger.info("Getting random image for " + subBreed + " sub-breed of " + breed + " from: " + url);
        return makeGetRequest(url);
    }

    /**
     * Get multiple random images for a specific breed
     * 
     * @param breed Dog breed to get random images for
     * @param count Number of random images to get
     * @return Response object containing random image URLs for the specified breed
     */
    public Response getMultipleRandomImagesByBreed(String breed, int count) {
        String url = BASE_URL + "/breed/" + breed + "/images/random/" + count;
        logger.info("Getting " + count + " random images for " + breed + " from: " + url);
        return makeGetRequest(url);
    }
}
