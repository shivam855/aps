package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * ixigo "Review Flight Details" page after selecting a flight.
 */
public class IxigoFlightReviewPage extends BasePage {

    private static final By CONTINUE_BUTTON = By.xpath(
            "//button[normalize-space()='Continue']");

    public void waitForReviewPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains("Review"),
                ExpectedConditions.urlContains("/flight/booking")));
        log.info("Review flight page loaded: " + getCurrentUrl());
    }

    public void clickContinue() {
        waitForReviewPage();
        WebElement continueBtn = waitForClickable(CONTINUE_BUTTON);
        continueBtn.click();
        log.info("Clicked Continue on review page");
    }
}
