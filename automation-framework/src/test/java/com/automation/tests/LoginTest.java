package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.pages.LoginPage;
import com.automation.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * LoginTest - Sample test class demonstrating:
 * 1. Extending BaseTest (auto browser setup/teardown)
 * 2. Using Page Objects (LoginPage)
 * 3. Reading test data from Excel via @DataProvider
 */
public class LoginTest extends BaseTest {

    // --------------------------------------------------------
    // Test 1: Simple hardcoded login test
    // --------------------------------------------------------
    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        log.info("Starting: testValidLogin");
        LoginPage loginPage = new LoginPage();
        loginPage.login("admin@example.com", "Admin@123");

        // Assert user is logged in (check page title or URL)
        String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("dashboard"),
                "Expected to land on dashboard. Actual URL: " + currentUrl);
        log.info("testValidLogin PASSED");
    }

    // --------------------------------------------------------
    // Test 2: Data-driven login using Excel
    // --------------------------------------------------------
    @Test(
        dataProvider  = "loginData",
        description   = "Verify login with multiple credential sets from Excel"
    )
    @SuppressWarnings("unchecked")
    public void testLoginWithExcelData(Map<String, String> testData) {
        String username    = testData.get("Username");
        String password    = testData.get("Password");
        String expectedResult = testData.get("ExpectedResult");  // "Pass" or "Fail"

        log.info("Testing login for: " + username + " | Expected: " + expectedResult);

        LoginPage loginPage = new LoginPage();
        loginPage.login(username, password);

        if ("Pass".equalsIgnoreCase(expectedResult)) {
            Assert.assertTrue(getDriver().getCurrentUrl().contains("dashboard"),
                    "Login should have succeeded for: " + username);
        } else {
            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Error message should appear for invalid credentials: " + username);
        }
    }

    // --------------------------------------------------------
    // DataProvider - reads from Excel file
    // --------------------------------------------------------
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtil.getDataAsArray(
                ConfigReader.getTestDataPath(),  // path from config.properties
                "LoginData"                      // sheet name in Excel
        );
    }
}
