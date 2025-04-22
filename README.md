# QA Automation Assessment

This project is an automation framework developed for the CIB Digital Tech QA Automation Assessment. It demonstrates automation of both API and Web UI tests using an open-source hybrid approach with modularization.

## Tools and Technologies Used

- Java 11
- Maven
- Cucumber (BDD Framework)
- Selenium WebDriver (Web UI Testing)
- RestAssured (API Testing)
- JUnit (Test Runner)
- ExtentReports (Reporting)
- Jackson (JSON Parsing)
- OpenCSV (CSV Parsing)
- Apache POI (Excel Parsing)
- Log4j (Logging)

## Framework Structure

The framework follows a hybrid approach with modularization and uses the Page Object Model design pattern. Key components include:

- **BDD Features**: Written in Gherkin syntax in feature files
- **Step Definitions**: Java classes that implement the steps in feature files
- **Page Objects**: Classes that encapsulate page elements and interactions
- **Utilities**: Helper classes for API testing, data reading, and WebDriver management
- **Test Data**: Stored in JSON and CSV formats
- **Configuration**: Properties file for framework configuration

## Project Structure

