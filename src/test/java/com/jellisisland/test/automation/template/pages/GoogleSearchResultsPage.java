package com.jellisisland.test.automation.template.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

public class GoogleSearchResultsPage extends PageObject {

    @FindBy(id = "search")
    private WebElementFacade searchResultsContainer;

    @FindBy(css = "#search .g")
    private WebElementFacade firstSearchResult;
}