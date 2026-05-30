package com.automation.pages;

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
 * Page object for ixigo train search — Selenium port of the Playwright flow.
 */
public class IxigoTrainsPage extends BasePage {

    private static final By ORIGIN_INPUT = By.cssSelector(
            "[data-testid='search-form-origin'] [data-testid='autocompleter-input'], "
                    + "input[placeholder*='Origin'], input[aria-label*='Origin']");
    private static final By DESTINATION_INPUT = By.cssSelector(
            "[data-testid='search-form-destination'] [data-testid='autocompleter-input'], "
                    + "input[placeholder*='Destination'], input[aria-label*='Destination']");
    private static final By BOOK_TRAIN_TICKETS = By.cssSelector(
            "[data-testid='book-train-tickets'], button[type='submit']");
    private static final By SEARCH_BUTTON = By.xpath(
            "//button[normalize-space()='Search' or contains(@data-testid,'book-train')]");
    private static final By RESULTS_HEADING = By.cssSelector("h1, h2, h3, [role='heading']");

    public void openTrainsPage(String url) {
        driver.get(url);
        log.info("Opened ixigo trains page: " + url);
        dismissOverlaysIfPresent();
    }

    public void selectOrigin(String query, String stationLabel) {
        WebElement origin = waitForClickable(ORIGIN_INPUT);
        origin.click();
        origin.clear();
        origin.sendKeys(query);
        log.info("Entered origin query: " + query);
        clickAutocompleteOption(stationLabel);
    }

    public void selectDestination(String query, String stationLabel) {
        WebElement destination = waitForClickable(DESTINATION_INPUT);
        destination.click();
        destination.clear();
        destination.sendKeys(query);
        log.info("Entered destination query: " + query);
        clickAutocompleteOption(stationLabel);
    }

    /**
     * Playwright flow: click day chip (Fri) then calendar date (May 30,).
     * Also supports ixigo quick chips: Tomorrow, Day After.
     * All parts are optional — skips silently when not configured or not visible.
     */
    public void selectJourneyDateOptions(String dayOfWeek, String dateButtonLabel, String quickChip) {
        if (quickChip != null && !quickChip.isBlank() && !"skip".equalsIgnoreCase(quickChip)) {
            if (clickByVisibleText(quickChip)) {
                log.info("Selected quick date chip: " + quickChip);
                return;
            }
        }

        selectDayOfWeek(dayOfWeek);
        selectCalendarDate(dateButtonLabel);
    }

    public void selectDayOfWeek(String day) {
        if (day == null || day.isBlank() || "skip".equalsIgnoreCase(day)) {
            return;
        }
        if (clickByVisibleText(day)) {
            log.info("Selected day of week: " + day);
        } else {
            log.debug("Day chip not visible, skipping: " + day);
        }
    }

    public void selectCalendarDate(String dateButtonLabel) {
        if (dateButtonLabel == null || dateButtonLabel.isBlank() || "skip".equalsIgnoreCase(dateButtonLabel)) {
            return;
        }
        if (tryClickDateButton(dateButtonLabel)) {
            log.info("Selected journey date: " + dateButtonLabel);
            return;
        }

        String todayLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d,", Locale.ENGLISH));
        if (tryClickDateButton(todayLabel)) {
            log.info("Selected today's date: " + todayLabel);
            return;
        }

        log.debug("Calendar date not changed — using default departure date on form");
    }

    public void clickBookTrainTickets() {
        By primary = By.cssSelector("[data-testid='book-train-tickets']");
        try {
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(primary));
            scrollIntoView(button);
            waitForClickable(primary).click();
        } catch (TimeoutException e) {
            WebElement search = waitForClickable(SEARCH_BUTTON);
            scrollIntoView(search);
            search.click();
        }
        log.info("Clicked Book Train Tickets / Search");
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    public void searchTrains(String originQuery, String originStation,
                             String destinationQuery, String destinationStation,
                             String dayOfWeek, String journeyDate, String quickChip) {
        selectOrigin(originQuery, originStation);
        selectDestination(destinationQuery, destinationStation);
        selectJourneyDateOptions(dayOfWeek, journeyDate, quickChip);
        clickBookTrainTickets();
    }

    public void waitForResultsPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("search-pwa"),
                ExpectedConditions.urlContains("between-stations"),
                ExpectedConditions.urlContains("delhi-kanpur")));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(., 'Delhi') and contains(., 'Kanpur')]")),
                ExpectedConditions.presenceOfElementLocated(RESULTS_HEADING)));
    }

    public String getResultsHeadingText() {
        waitForResultsPage();
        List<WebElement> matches = driver.findElements(
                By.xpath("//*[contains(., 'Delhi') and contains(., 'Kanpur')]"));
        for (WebElement match : matches) {
            if (match.isDisplayed()) {
                String text = match.getText().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        List<WebElement> headings = driver.findElements(RESULTS_HEADING);
        for (WebElement heading : headings) {
            if (heading.isDisplayed() && !heading.getText().trim().isEmpty()) {
                return heading.getText().trim();
            }
        }
        return "";
    }

    public boolean resultsHeadingContains(String expectedText) {
        return getResultsHeadingText().contains(expectedText);
    }

    private void clickAutocompleteOption(String stationLabel) {
        String partial = stationLabel.replace(" (", "(").replace(") ", ")");
        By option = By.xpath(
                "(//li[contains(normalize-space(), \"" + stationLabel + "\") "
                        + "or contains(normalize-space(), \"" + partial + "\")]"
                        + " | //*[@role='option' or @role='listitem'][contains(., \"" + stationLabel + "\")]"
                        + " | //*[contains(@class,'suggestion') or contains(@class,'autocomplete')]"
                        + "//*[contains(normalize-space(), \"" + stationLabel + "\")])[1]");
        waitForClickable(option).click();
        log.debug("Selected autocomplete option: " + stationLabel);
    }

    private boolean clickByVisibleText(String text) {
        By locator = By.xpath("(//*[normalize-space()='" + text + "'"
                + " or contains(normalize-space(), '" + text + "')]"
                + "[self::button or self::div or self::span or self::a])[1]");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean tryClickDateButton(String dateButtonLabel) {
        By dateButton = By.xpath("//button[contains(normalize-space(), '" + dateButtonLabel + "')]");
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.elementToBeClickable(dateButton)).click();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void dismissOverlaysIfPresent() {
        try {
            List<WebElement> dismissButtons = driver.findElements(By.xpath(
                    "//button[contains(.,'Accept') or contains(.,'Got it') or contains(.,'Close')]"));
            for (WebElement button : dismissButtons) {
                if (button.isDisplayed()) {
                    button.click();
                    log.debug("Dismissed overlay/popup");
                    break;
                }
            }
        } catch (Exception e) {
            log.debug("No overlay to dismiss");
        }
    }
}
