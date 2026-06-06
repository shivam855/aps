package com.automation.pages;

import com.automation.base.DriverManager;
import com.automation.config.ConfigReader;
import com.automation.utils.LoggerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
    protected static final LoggerUtil log = LoggerUtil.getLogger(BasePage.class);

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
    /**
	 * Wait for specific ExpectedCondition for the given amount of time in seconds
	 */
	@SuppressWarnings("unchecked")
	private void waitFor(@SuppressWarnings("rawtypes") ExpectedCondition condition, Integer timeOutInSeconds) {
		timeOutInSeconds = timeOutInSeconds != null ? timeOutInSeconds : 30;
		wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
		wait.until(condition);
	}


	/**
	 * Wait for given number of seconds for element with given locator to be visible
	 * on the page
	 */
    protected void waitForVisibilityOf(By locator, Integer... timeOutInSeconds) {
		int attempts = 0;
		while (attempts < 2) {
			try {
				waitFor(ExpectedConditions.visibilityOfElementLocated(locator),
						(timeOutInSeconds.length > 0 ? timeOutInSeconds[0] : null));
				break;
			} catch (Exception e) {
			}
			attempts++;
		}
	}


	protected void waitForInvisibilityOf(By locator, Integer... timeOutInSeconds) {
		int attempts = 0;
		while (attempts < 2) {
			try {
				waitFor(ExpectedConditions.invisibilityOfElementLocated(locator),
						(timeOutInSeconds.length > 0 ? timeOutInSeconds[0] : null));
				break;
			} catch (Exception e) {
			}
			attempts++;
		}
	}
}
