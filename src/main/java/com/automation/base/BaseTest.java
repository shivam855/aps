package com.automation.base;

import com.automation.config.ConfigReader;
import com.automation.reporting.ExtentReportManager;
import com.automation.utils.LoggerUtil;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * BaseTest - Parent class for all test classes.
 * Handles browser setup, teardown, failure screenshots, and framework logging.
 */
@Listeners({com.automation.listeners.ExtentTestListener.class})
public class BaseTest {

    protected static final LoggerUtil log = LoggerUtil.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        log.info("=== Starting Browser Setup ===");
        DriverManager.initDriver();
        WebDriver driver = DriverManager.getDriver();
        String startUrl = ConfigReader.getStartUrl();
        driver.get(startUrl);
        log.info("Navigated to: " + startUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                if (ConfigReader.isScreenshotOnFail()) {
                    ExtentReportManager.failWithScreenshot(
                            "Test FAILED: " + result.getName(),
                            result.getThrowable());
                } else {
                    ExtentTest target = ExtentReportManager.getActiveTarget();
                    if (target != null) {
                        target.fail("Test FAILED: " + result.getName());
                        if (result.getThrowable() != null) {
                            target.fail(result.getThrowable());
                        }
                    }
                }
                log.fail("Test FAILED: " + result.getName(), result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                ExtentReportManager.finishCurrentStep();
                ExtentTest test = ExtentReportManager.getTest();
                if (test != null) {
                    test.pass("Test passed");
                }
                log.info("Test PASSED: " + result.getName());
            } else if (result.getStatus() == ITestResult.SKIP) {
                ExtentTest test = ExtentReportManager.getTest();
                if (test != null) {
                    test.skip("Test skipped");
                }
            }
        } finally {
            ExtentReportManager.removeTest();
           // DriverManager.quitDriver();
            log.info("=== Browser Closed ===");
        }
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
