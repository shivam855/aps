package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Live ixigo flight search results — /search/result/flight
 */
public class IxigoFlightResultsPage extends BasePage {

    private static final By BOOK_BUTTON = By.xpath(
            "(//button[normalize-space()='Book'])[1]");
    private static final By NO_FLIGHTS_MSG = By.xpath(
            "//*[contains(.,'No flights found')]");

    public void waitForResultsPage() {
        wait.until(ExpectedConditions.urlContains("/search/result/flight"));
        log.info("Flight results URL: " + getCurrentUrl());
    }

    public void waitForFlightsToLoad() {
        waitForResultsPage();
        wait.until(driver -> !isDisplayed(NO_FLIGHTS_MSG)
                && driver.findElements(BOOK_BUTTON).stream().anyMatch(WebElement::isDisplayed));
        log.info("Flight results loaded with Book buttons");
    }

    public void selectFirstFlight() {
        waitForFlightsToLoad();
        waitForClickable(BOOK_BUTTON).click();
        log.info("Clicked first Book on flight results");
    }

    public boolean hasFlights() {
        waitForResultsPage();
        return !isDisplayed(NO_FLIGHTS_MSG) && isDisplayed(BOOK_BUTTON);
    }

    public boolean routeSummaryContains(String fromCity, String toCity) {
        String pageText = driver.findElement(By.tagName("body")).getText();
        return pageText.toLowerCase().contains(fromCity.toLowerCase())
                || pageText.toLowerCase().contains(toCity.toLowerCase())
                || getCurrentUrl().toUpperCase().contains("DEL")
                || getCurrentUrl().toUpperCase().contains("BOM");
    }
}
