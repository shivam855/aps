package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.pages.DashboardPage;
import com.automation.pages.LoginPage;
import com.automation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Sample automation against mock login page with mock login / dashboard / train-search APIs.
 */
public class SampleAutomationTest extends BaseTest {

    private final LoggerUtil logger = LoggerUtil.getLogger(SampleAutomationTest.class);

    @Test(description = "Sample: login via mock API and verify dashboard train search results")
    public void testMockLoginSuccess() {
        logger.info("=== Sample Automation Test Started ===");
        logger.debug("Mock page URL: " + ConfigReader.getMockPageUrl());
        logger.debug("Mock API base URL: " + ConfigReader.getMockApiBaseUrl());

        logger.step("Step 1: Open mock login page");
        logger.debug("Page title: " + getDriver().getTitle());

        logger.step("Step 2: Login — triggers mock auth API (not real ixigo API)");
        LoginPage loginPage = new LoginPage();
        loginPage.login("admin@example.com", "Admin@123");

        logger.step("Step 3: Verify dashboard loaded from mock APIs");
        DashboardPage dashboard = new DashboardPage();
        dashboard.waitForDashboard();
        dashboard.waitForTrainResults();

        Assert.assertTrue(getDriver().getCurrentUrl().contains("dashboard"),
                "Expected URL to contain 'dashboard'");
        Assert.assertTrue(dashboard.getWelcomeMessage().contains("Welcome"),
                "Dashboard welcome message should be displayed");
        Assert.assertTrue(dashboard.getSearchSummary().contains("New Delhi"),
                "Train search summary should show route from mock API");
        Assert.assertTrue(dashboard.getTrainResultCount() >= 1,
                "At least one train should be listed from mock search API");
        Assert.assertTrue(dashboard.isTrainListed("12951"),
                "Mock train 12951 (Mumbai Rajdhani) should appear in results");

        logger.info("=== Sample Automation Test PASSED ===");
    }

    @Test(description = "Sample: invalid login returns mock API error")
    public void testMockLoginFailure() {
        logger.info("=== Negative Login Test Started ===");

        logger.step("Step 1: Login with invalid credentials via mock API");
        LoginPage loginPage = new LoginPage();
        loginPage.login("wrong@example.com", "wrongpass");

        logger.step("Step 2: Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should appear for invalid credentials");

        logger.info("=== Negative Login Test PASSED ===");
    }

    @Test(description = "Demo: screenshot attached at failing step", enabled = false)
    public void testFailureScreenshotAtStep() {
        logger.step("Step 1: Open mock login page");
        LoginPage loginPage = new LoginPage();
        loginPage.login("admin@example.com", "Admin@123");

        logger.step("Step 3: Verify dashboard (intentional failure for screenshot demo)");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("nonexistent-page"),
                "Intentional failure to demonstrate step-level screenshot in report");
    }
}
