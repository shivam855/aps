package com.automation.base;

import com.automation.config.ConfigReader;
import com.automation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Parent class for all test classes.
 * Handles browser setup, teardown, and failure screenshots.
 *
 * Every test class should extend this class:
 *   public class LoginTest extends BaseTest { ... }
 */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    /**
     * Runs BEFORE each test method.
     * Opens browser and navigates to base URL.
     */
    @BeforeMethod
    public void setUp() {
        log.info("=== Starting Browser Setup ===");
        DriverManager.initDriver();
        WebDriver driver = DriverManager.getDriver();
        driver.get(ConfigReader.getBaseUrl());
        log.info("Navigated to: " + ConfigReader.getBaseUrl());
    }

    /**
     * Runs AFTER each test method.
     * Takes screenshot on failure, then closes browser.
     */
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test FAILED: " + result.getName());
            if (ConfigReader.isScreenshotOnFail()) {
                ScreenshotUtil.takeScreenshot(result.getName());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("Test PASSED: " + result.getName());
        }
        DriverManager.quitDriver();
        log.info("=== Browser Closed ===");
    }

    /** Convenience method to get driver in test classes */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
