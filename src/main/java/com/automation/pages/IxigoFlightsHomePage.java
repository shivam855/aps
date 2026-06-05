package com.automation.pages;

import com.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * ixigo.com/flights — live search form (origin / destination / search).
 */
public class IxigoFlightsHomePage extends BasePage {

    private static final By ORIGIN_TRIGGER = By.cssSelector("[data-testid='originId']");
    private static final By DESTINATION_TRIGGER = By.cssSelector("[data-testid='destinationId']");
    private static final By DEPARTURE_TRIGGER = By.cssSelector("[data-testid='departureDate']");
    private static final By OVERLAY_INPUT = By.cssSelector("input.outline-none, input[type='text']");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");

    public void openFlightsHome(String url) {
        driver.get(url);
        dismissOverlaysIfPresent();
        log.info("Opened ixigo flights: " + url);
    }

    public void searchFlights(String fromQuery, String fromAirport, String toQuery, String toAirport,
                              String departDate) {
        selectAirport(ORIGIN_TRIGGER, fromQuery, fromAirport);
        selectAirport(DESTINATION_TRIGGER, toQuery, toAirport);
        selectDepartureDateIfProvided(departDate);
        clickSearch();
    }

    private void selectAirport(By trigger, String query, String airportLabel) {
        waitForClickable(trigger).click();
        WebElement input = waitForOverlayInput();
        input.click();
        input.clear();
        input.sendKeys(query);
        log.info("Typed airport query: " + query);
        clickAirportSuggestion(airportLabel);
    }

    private WebElement waitForOverlayInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(OVERLAY_INPUT));
    }

    private void clickAirportSuggestion(String airportLabel) {
        String normalized = airportLabel.trim();
        By option = By.xpath(
                "(//*[normalize-space()=\"" + normalized + "\"]"
                        + " | //*[contains(normalize-space(),\"" + normalized + "\")]"
                        + "[self::p or self::li or self::div][1])[1]");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
            shortWait.until(ExpectedConditions.elementToBeClickable(option)).click();
            log.info("Selected airport: " + normalized);
        } catch (TimeoutException e) {
            By partial = By.xpath(
                    "(//*[contains(normalize-space(),\"" + normalized.split(",")[0].trim() + "\")]"
                            + "[self::p or self::li][1])[1]");
            waitForClickable(partial).click();
            log.info("Selected airport (partial match): " + normalized);
        }
    }

    private void selectDepartureDateIfProvided(String departDate) {
        if (departDate == null || departDate.isBlank() || "skip".equalsIgnoreCase(departDate)) {
            return;
        }
        try {
            waitForClickable(DEPARTURE_TRIGGER).click();
            if (departDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(departDate);
                String label = date.format(DateTimeFormatter.ofPattern("MMM d,", Locale.ENGLISH));
                if (clickByVisibleText(label)) {
                    log.info("Selected departure date: " + label);
                    return;
                }
            }
            if (clickByVisibleText(departDate)) {
                log.info("Selected departure date chip: " + departDate);
            }
        } catch (Exception e) {
            log.debug("Departure date not changed — using default on form");
        }
    }

    public void clickSearch() {
        WebElement search = waitForClickable(SEARCH_BUTTON);
        scrollIntoView(search);
        search.click();
        log.info("Clicked Search flights");
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
    }

    private boolean clickByVisibleText(String text) {
        By locator = By.xpath(
                "(//*[normalize-space()='" + text + "' or contains(normalize-space(),'" + text + "')]"
                        + "[self::button or self::div or self::span])[1]");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void dismissOverlaysIfPresent() {
        try {
            List<WebElement> dismissButtons = driver.findElements(By.xpath(
                    "//button[contains(.,'Accept') or contains(.,'Got it') or contains(.,'Close')"
                            + " or contains(.,'Not Now')]"));
            for (WebElement button : dismissButtons) {
                if (button.isDisplayed()) {
                    button.click();
                    log.debug("Dismissed overlay");
                    break;
                }
            }
        } catch (Exception e) {
            log.debug("No overlay to dismiss");
        }
    }

    public void waitForSearchForm() {
        waitForVisible(ORIGIN_TRIGGER);
    }
}
