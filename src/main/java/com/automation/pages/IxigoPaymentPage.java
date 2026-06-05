package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Payment step on live ixigo — fare pay / UPI / card options.
 */
public class IxigoPaymentPage extends BasePage {

    private static final By PAYMENT_INDICATORS = By.xpath(
            "//*[contains(.,'Payment') or contains(.,'Pay Now') or contains(.,'UPI')"
            + " or contains(.,'Pay Securely') or contains(.,'Total Amount')]"
            + "[self::h1 or self::h2 or self::h3 or self::button or self::p][1]");

    public void waitForPaymentScreen() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("payment"),
                ExpectedConditions.urlContains("checkout"),
                ExpectedConditions.urlContains("pay"),
                ExpectedConditions.presenceOfElementLocated(PAYMENT_INDICATORS)));
        log.info("Payment step reached: " + getCurrentUrl());
    }

    public boolean isPaymentScreenDisplayed() {
        try {
            waitForPaymentScreen();
            return true;
        } catch (Exception e) {
            String body = driver.findElement(By.tagName("body")).getText().toLowerCase();
            return body.contains("payment") || body.contains("pay now") || body.contains("upi");
        }
    }

    public String getPaymentHeading() {
        try {
            return getText(PAYMENT_INDICATORS);
        } catch (Exception e) {
            return driver.getTitle();
        }
    }

    public boolean paymentSummaryContainsRoute(String fromCity, String toCity) {
        String text = driver.findElement(By.tagName("body")).getText().toLowerCase();
        return text.contains(fromCity.toLowerCase()) || text.contains(toCity.toLowerCase());
    }
}
