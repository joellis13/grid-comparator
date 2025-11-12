package com.jellisisland.test.automation.template.steps;

import com.jellisisland.test.automation.template.pages.CaptchaPage;
import com.jellisisland.test.automation.template.pages.GoogleHomePage;
import com.jellisisland.test.automation.template.pages.GoogleSearchResultsPage;
import net.serenitybdd.annotations.Step;

public class GoogleSearchSteps {

    GoogleHomePage googleHomePage;
    GoogleSearchResultsPage googleSearchResultsPage;
    CaptchaPage captchaPage;

    @Step("Open Google homepage")
    public void openGoogleHomepage() {
        googleHomePage.open();
    }

    @Step("Search for '{0}'")
    public void searchFor(String searchTerm) {
        googleHomePage.enterSearchTerm(searchTerm);
        googleHomePage.clickSearchButton();
    }

    @Step("Verify Captcha is displayed")
    public void verifyCaptchaIsDisplayed() {
        captchaPage.getRecaptcha().shouldBeVisible();
    }
}