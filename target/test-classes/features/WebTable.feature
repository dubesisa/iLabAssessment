Feature: Web Table User Management

  @web @table
  Scenario Outline: Add a new user to the web table
    Given I navigate to the web table application
    And I am on the User List Table page
    When I click on Add User button
    And I enter user details:
      | FirstName   | LastName   | UserName   | Password   | Customer   | Role   | Email   | CellPhone   |
      | <FirstName> | <LastName> | <UserName> | <Password> | <Customer> | <Role> | <Email> | <CellPhone> |
    And I click on Save button
    Then the user should be added to the User List Table
    And the user details should match the entered data

    Examples:
      | FirstName | LastName | UserName     | Password  | Customer | Role     | Email              | CellPhone   |
      | John      | Doe      | johndoe{TS}  | password1 | Company A| Admin    | john@example.com   | 1234567890  |
      | Jane      | Smith    | janesmith{TS}| password2 | Company B| Customer | jane@example.com   | 0987654321  |

  @web @table @csv
  Scenario: Add multiple users from CSV data file
    Given I navigate to the web table application
    And I am on the User List Table page
    When I add users from CSV data file
    Then all users should be added to the User List Table

  @web @table @json
  Scenario: Add multiple users from JSON data file
    Given I navigate to the web table application
    And I am on the User List Table page
    When I add users from JSON data file
    Then all users should be added to the User List Table
