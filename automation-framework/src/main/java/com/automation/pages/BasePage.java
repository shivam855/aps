package com.automation.pages;

import com.automation.base.DriverManager;
import com.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Parent class for all Page Object classes.
 * Contains common reusable actions: click, type, getText, waitFor, etc.
 *
 * All page classes should extend this:
 *   public class LoginPage extends BasePage { ... }
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        PageFactory.initElements(driver, this);  // Initialize @FindBy elements
    }

    // --------------------------------------------------
    // Core Actions
    // --------------------------------------------------

    protected void click(WebElement element) {
        waitForClickable(element).click();
        log.debug("Clicked element: " + element);
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(WebElement element, String text) {
        waitForVisible(element).clear();
        element.sendKeys(text);
        log.debug("Typed '" + text + "' into element");
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        return waitForVisible(element).getText().trim();
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // --------------------------------------------------
    // Wait Methods
    // --------------------------------------------------

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
