package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Traveller / contact details on live ixigo booking flow.
 */
public class IxigoTravellerDetailsPage extends BasePage {

    private static final By CONTINUE_BUTTON = By.xpath(
            "//button[normalize-space()='Continue' or normalize-space()='Proceed to Pay'"
            + " or contains(normalize-space(),'Proceed')]");

    public void waitForTravellerSection() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(.,'Traveller') or contains(.,'Passenger')"
                                + " or contains(.,'Contact')]")),
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("input[placeholder*='First'], input[placeholder*='Name'],"
                                + " input[placeholder*='Email'], input[placeholder*='Mobile']"))));
        log.info("Traveller / contact section visible");
    }

    public void fillTravellerDetails(String passengerName, String mobile, String email) {
        waitForTravellerSection();
        fillIfPresent(By.cssSelector(
                "input[placeholder*='First'], input[name*='first'], input[aria-label*='First']"),
                splitFirstName(passengerName));
        fillIfPresent(By.cssSelector(
                "input[placeholder*='Last'], input[name*='last'], input[aria-label*='Last']"),
                splitLastName(passengerName));
        fillIfPresent(By.cssSelector(
                "input[placeholder*='Name'], input[placeholder*='Full']"),
                passengerName);
        fillIfPresent(By.cssSelector(
                "input[placeholder*='Mobile'], input[type='tel']"),
                mobile);
        fillIfPresent(By.cssSelector(
                "input[placeholder*='Email'], input[type='email']"),
                email);
        log.info("Filled traveller/contact details");
    }

    public void continueToPayment() {
        if (isDisplayed(CONTINUE_BUTTON)) {
            waitForClickable(CONTINUE_BUTTON).click();
            log.info("Clicked Continue / Proceed to payment");
        }
    }

    private void fillIfPresent(By locator, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        try {
            WebElement field = driver.findElement(locator);
            if (field.isDisplayed()) {
                field.clear();
                field.sendKeys(value);
            }
        } catch (Exception e) {
            log.debug("Field not present: " + locator);
        }
    }

    private String splitFirstName(String fullName) {
        if (fullName == null || !fullName.contains(" ")) {
            return fullName != null ? fullName : "";
        }
        return fullName.substring(0, fullName.indexOf(' ')).trim();
    }

    private String splitLastName(String fullName) {
        if (fullName == null || !fullName.contains(" ")) {
            return "User";
        }
        return fullName.substring(fullName.indexOf(' ') + 1).trim();
    }
}
