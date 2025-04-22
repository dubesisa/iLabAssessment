Feature: Dog API Tests

  @api @dog
  Scenario: Verify retriever breed exists in the list of all dog breeds
    Given I want to get a list of all dog breeds
    When I make a request to get all dog breeds
    Then the API call should be successful with status code 200
    And the response should contain a list of breeds
    And the list should contain "retriever" breed

  @api @dog
  Scenario: Verify retrieving sub-breeds for retriever breed
    Given I want to get a list of sub-breeds for "retriever"
    When I make a request to get sub-breeds for "retriever"
    Then the API call should be successful with status code 200
    And the response should contain a list of sub-breeds
    And I should store the list of sub-breeds for validation

  @api @dog
  Scenario: Get a random image for the golden sub-breed of retriever
    Given I want to get a random image for "golden" sub-breed of "retriever"
    When I make a request to get a random image for "golden" sub-breed of "retriever"
    Then the API call should be successful with status code 200
    And the response should contain a random image URL
    And the image URL should be valid
