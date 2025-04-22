Feature: Web Table User Management

  @web @table
  Scenario Outline: Add a new user to the web table
    Given I navigate to the web table application
    And I am on the User List Table page
    When I click on Add User button
    And I enter user details:
      | FirstName|LastName|UserName| Password| Customer|Role |Email|CellPhone|
      |FName1|LName1|User1|Pass1|Company AAA|Admin|admin@mail.com|082555|
      |FName2|LName2|User2|Pass2|Company BBB|Customer|customer@mail.com|083444|
    And I click on Save button
    Then the user should be added to the User List Table
    And the user details should match the entered data

    Examples:
      | FirstName | LastName | UserName     | Password  | Customer | Role     | Email              | CellPhone   |
      | John      | Doe      | johndoe | password1 | Company AAA| Admin    | john@example.com   | 1234567890  |


