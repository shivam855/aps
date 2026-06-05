package com.automation.pages;

import com.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Live ixigo login — mobile number + OTP (OTP entered manually during the wait window).
 */
public class IxigoLoginPage extends BasePage {

    private static final By LOGIN_BUTTON = By.xpath(
            "//button[contains(normalize-space(),'Log in') or contains(normalize-space(),'Sign up')]");
    private static final By MOBILE_INPUT = By.cssSelector(
            "input[placeholder*='Mobile'], input[type='tel']");
    private static final By CONTINUE_BUTTON = By.xpath(
            "//button[normalize-space()='Continue']");
    private static final By OTP_INPUT = By.cssSelector(
            "input[placeholder*='OTP'], input[autocomplete='one-time-code'], "
                    + "input[inputmode='numeric'][maxlength='6'], input[maxlength='4']");

    public void openLoginModal() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginBtn = shortWait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        loginBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MOBILE_INPUT));
        log.info("Opened ixigo login modal");
    }

    public void enterMobile(String mobile) {
        WebElement mobileField = waitForVisible(MOBILE_INPUT);
        mobileField.clear();
        mobileField.sendKeys(mobile);
        log.info("Entered mobile number");
    }

    public void clickContinue() {
        waitForClickable(CONTINUE_BUTTON).click();
        log.info("Clicked Continue on login");
    }

    /**
     * Sends OTP request, then pauses so you can enter the OTP on the ixigo site manually.
     */
    public void loginWithMobileOtp(String mobile) {
        if (ConfigReader.isSkipLogin()) {
            log.info("Login skipped (ixigo.skip.login=true)");
            return;
        }
        openLoginModal();
        enterMobile(mobile);
        clickContinue();
        waitForManualOtpEntry();
    }

    /**
     * When login appears mid-booking (e.g. after Continue on review page).
     */
    public void completeLoginIfPrompted(String mobile) {
        if (ConfigReader.isSkipLogin()) {
            return;
        }
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            if (shortWait.until(ExpectedConditions.visibilityOfElementLocated(MOBILE_INPUT)).isDisplayed()) {
                log.info("Login prompt detected — entering mobile");
                enterMobile(mobile);
                clickContinue();
                waitForManualOtpEntry();
            }
        } catch (Exception e) {
            log.debug("No login prompt on screen");
        }
    }

    public void waitForManualOtpEntry() {
        int waitSeconds = ConfigReader.getIxigoOtpWaitSeconds();
        log.info(">>> Enter OTP manually on ixigo within " + waitSeconds + " seconds <<<");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(MOBILE_INPUT));
            log.info("Login modal closed — assuming OTP accepted");
        } catch (Exception e) {
            try {
                Thread.sleep(waitSeconds * 1000L);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            log.info("OTP wait window finished");
        }
    }

    public boolean isOtpFieldVisible() {
        return isDisplayed(OTP_INPUT);
    }
}
