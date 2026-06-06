package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.ExpediaFlightPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Sample test class demonstrating:
 * 1. Extending BaseTest (auto browser setup/teardown)
 * 2. Using Page Objects (LoginPage)
 * 3. Reading test data from Excel via @DataProvider
 */
public class ExpediaJournyTest extends BaseTest {

    // --------------------------------------------------------
    // Test 1: Search flights on Expedia and verify results show up
    // --------------------------------------------------------


    @Test(description = "Search flights on Expedia and verify results show up")
    public void testExpediaFlightSearch() {
        log.info("Starting: testExpediaFlightSearch");
        ExpediaFlightPage flightPage = new ExpediaFlightPage();
        // example: choose a specific date aria-label text present on Expedia
        String dateAria = "Thursday, August 22, 2024";
        flightPage.searchFlight("Kolkata", "Hyderabad", dateAria);

        // verify at least one result appears
        int resultCount = getDriver().findElements(
                By.xpath("//li[@data-test-id='offer-listing']")
        ).size();
        Assert.assertTrue(resultCount > 0, "Expected flight results to be present");
        log.info("testExpediaFlightSearch PASSED");
    }
}
