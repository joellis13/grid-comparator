package com.jellisisland.test.automation.template.pages;

import lombok.Getter;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class CaptchaPage extends PageObject {

    @FindBy(id = "recaptcha")
    private WebElementFacade recaptcha;
}