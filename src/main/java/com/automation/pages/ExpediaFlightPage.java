package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * ExpediaFlightPage - encapsulates actions to perform a flight search on Expedia
 */
public class ExpediaFlightPage extends BasePage {

    private By flightsTab = By.cssSelector("a[aria-controls='search_form_product_selector_flights']");
    private By leavingFromButton = By.id("origin_select-input");
    private By leavingFromInput = By.id("origin_select-input");
    private By goingToButton = By.id("destination_select-input");
    private By goingToInput = By.id("destination_select-input");
    private By datePickerButton = By.cssSelector("button[@data-testid=\"uitk-date-selector-input1-default\"]");
    private By applyDateButton = By.xpath("//button[@data-stid='apply-date-selector' and text()='Done']");
    private By openTravelers = By.cssSelector("button[data-stid='open-room-picker']");
    private By adultIncrement = By.xpath("//div[contains(@class,'uitk-step-input-controls')]//button[@type='button' and not(@disabled)]");
    private By searchButton = By.cssSelector("button#search_button");
    private By firstFlightButton = By.xpath("//button[contains(@data-stid,'FLIGHTS_DETAILS_AND_FARES-index-')]");

    /**
     * Perform a flight search and return after results page is loaded.
     * @param origin city name (eg: Kolkata)
     * @param destination city name (eg: Hyderabad)
     * @param dateAriaLabel aria-label text for the date to click (eg: "Thursday, August 22, 2024")
     */
    public void searchFlight(String origin, String destination, String dateAriaLabel) {
        click(flightsTab);

        click(leavingFromButton);
        // enter origin and press Enter
        WebElement fromInput = waitForClickable(leavingFromInput);
        fromInput.clear();
        fromInput.sendKeys(origin);
        fromInput.sendKeys(Keys.ENTER);

        click(goingToButton);
        WebElement toInput = waitForClickable(goingToInput);
        toInput.clear();
        toInput.sendKeys(destination);
        toInput.sendKeys(Keys.ENTER);

        click(datePickerButton);
        // select date by matching aria-label
        By dateLocator = By.xpath("//*[@aria-label='" + dateAriaLabel + "']");
        click(dateLocator);
        click(applyDateButton);

        click(openTravelers);
        // increment adults once
        click(adultIncrement);

        click(searchButton);
        // wait for results to load (ExpediaResults has locators), delegate to results class if needed
        waitForVisibilityOf(By.xpath("//li[@data-test-id='offer-listing']"), 60);
    }

    public void selectFirstFlight() {
        click(firstFlightButton);
    }
}
