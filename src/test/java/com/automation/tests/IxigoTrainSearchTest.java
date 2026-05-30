package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.pages.IxigoTrainsPage;
import com.automation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Selenium equivalent of the Playwright ixigo train search flow:
 *
 * <pre>
 * page.goto('https://www.ixigo.com/trains');
 * origin: New delhi → New Delhi (NDLS)
 * destination: kanpur → Kanpur Central (CNB)
 * optional: day Fri + date May 30,  (or quick chip Tomorrow)
 * click Book Train Tickets
 * expect heading: Delhi to Kanpur Trains
 * </pre>
 */
public class IxigoTrainSearchTest extends BaseTest {

    private final LoggerUtil logger = LoggerUtil.getLogger(IxigoTrainSearchTest.class);

    @Test(description = "Search Delhi to Kanpur trains on ixigo flow")
    public void testDelhiToKanpurTrainSearch() {
        IxigoTrainsPage trainsPage = new IxigoTrainsPage();

        logger.step("Step 1: Verify ixigo trains page is open");
        logger.debug("Current URL: " + getDriver().getCurrentUrl());
        logger.debug("Page title: " + getDriver().getTitle());

        logger.step("Step 2: Select origin — New Delhi (NDLS)");
        trainsPage.selectOrigin(
                ConfigReader.getIxigoOriginQuery(),
                ConfigReader.getIxigoOriginStation());

        logger.step("Step 3: Select destination — Kanpur Central (CNB)");
        trainsPage.selectDestination(
                ConfigReader.getIxigoDestinationQuery(),
                ConfigReader.getIxigoDestinationStation());

        logger.step("Step 4: Select journey date (optional — Playwright: Fri + May 30,)");
        trainsPage.selectJourneyDateOptions(
                ConfigReader.getIxigoJourneyDay(),
                ConfigReader.getIxigoJourneyDate(),
                ConfigReader.getIxigoJourneyQuickChip());

        logger.step("Step 5: Click Book Train Tickets / Search");
        trainsPage.clickBookTrainTickets();

        logger.step("Step 6: Verify results heading");
        String expectedHeading = ConfigReader.getIxigoExpectedResultsHeading();
        String actualHeading = trainsPage.getResultsHeadingText();
        logger.debug("Results heading: " + actualHeading);

        Assert.assertTrue(actualHeading.contains(expectedHeading),
                "Expected heading to contain '" + expectedHeading + "'. Actual: " + actualHeading);

        logger.info("=== Ixigo Train Search Test PASSED ===");
    }
}
