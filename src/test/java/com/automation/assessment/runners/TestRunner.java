package com.automation.assessment.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.automation.assessment.stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty.html",
                "json:target/cucumber-reports/CucumberTestReport.json"
        },
        monochrome = true,
        dryRun = false,
        tags = ""
)
public class TestRunner {
    // This class serves as an entry point for running Cucumber tests
}
