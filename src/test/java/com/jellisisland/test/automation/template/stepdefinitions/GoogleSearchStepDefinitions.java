package com.jellisisland.test.automation.template.stepdefinitions;

import com.jellisisland.test.automation.template.steps.GoogleSearchSteps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;

public class GoogleSearchStepDefinitions {

    @Steps
    GoogleSearchSteps googleSearchSteps;

    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        googleSearchSteps.openGoogleHomepage();
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        googleSearchSteps.searchFor(searchTerm);
    }

    @Then("I should see the captcha challenge")
    public void iShouldSeeTheCaptchaChallenge() {
        googleSearchSteps.verifyCaptchaIsDisplayed();
    }
}