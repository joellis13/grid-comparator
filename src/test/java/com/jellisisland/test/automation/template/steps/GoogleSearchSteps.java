package com.jellisisland.test.automation.template.steps;

import com.jellisisland.test.automation.template.pages.CaptchaPage;
import com.jellisisland.test.automation.template.pages.GoogleHomePage;
import com.jellisisland.test.automation.template.pages.GoogleSearchResultsPage;
import net.serenitybdd.annotations.Step;
import org.openqa.selenium.WebDriver;
import com.jellisisland.test.automation.template.util.PerfReporter;

public class GoogleSearchSteps {

    GoogleHomePage googleHomePage;
    GoogleSearchResultsPage googleSearchResultsPage;
    CaptchaPage captchaPage;

    @Step("Open Google homepage")
    public void openGoogleHomepage() {
        googleHomePage.open();
        // Record navigation timing for performance comparison (will write to build/test-results/perf-results.csv)
        try {
            WebDriver driver = googleHomePage.getDriver();
            PerfReporter.recordNavigationTiming(driver);
        } catch (Exception e) {
            // best effort - don't fail the test on reporter errors
        }
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