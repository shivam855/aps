package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.pages.IxigoFlightResultsPage;
import com.automation.pages.IxigoFlightReviewPage;
import com.automation.pages.IxigoFlightsHomePage;
import com.automation.pages.IxigoLoginPage;
import com.automation.pages.IxigoPaymentPage;
import com.automation.pages.IxigoTravellerDetailsPage;
import com.automation.utils.ExcelUtil;
import com.automation.utils.LoggerUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Week 3 — Live ixigo.com flight booking: login → search → book → traveller → payment.
 * Data-driven from Excel sheet {@code FlightBookingData}.
 *
 * <p>OTP: After mobile is submitted, enter the OTP manually in the Chrome window within
 * {@code ixigo.otp.wait.seconds} (see config.properties).
 */
public class IxigoFlightBookingTest extends BaseTest {

    private final LoggerUtil logger = LoggerUtil.getLogger(IxigoFlightBookingTest.class);

    @Test(
            dataProvider = "flightBookingData",
            description = "Live ixigo flight booking from login through payment screen"
    )
    @SuppressWarnings("unchecked")
    public void testFlightBookingFlowFromExcel(Map<String, String> testData) throws InterruptedException {
        String mobile = testData.get("Mobile");
        String fromQuery = testData.get("FromCity");
        String fromAirport = testData.get("FromCity");
        String toQuery = testData.get("ToCity");
        String toAirport = testData.get("ToCity");
        String departDate = testData.getOrDefault("DepartDate", "skip");
        String passengerName = testData.get("PassengerName");
        String email = testData.get("Email");
        String expectedResult = testData.get("ExpectedResult");

        logger.info("=== Live ixigo: " + fromAirport + " → " + toAirport
                + " | Mobile: " + mobile + " | Expected: " + expectedResult + " ===");

        if (!"Pass".equalsIgnoreCase(expectedResult)) {
            logger.info("Skipping row — ExpectedResult is not Pass");
            return;
        }

        runLiveBookingFlow(mobile, fromQuery, fromAirport, toQuery, toAirport,
                departDate, passengerName, email);
    }

    private void runLiveBookingFlow(String mobile, String fromQuery, String fromAirport,
                                    String toQuery, String toAirport, String departDate,
                                    String passengerName, String email) throws InterruptedException {
        String flightsUrl = ConfigReader.getFlightsUrl();
        IxigoFlightsHomePage homePage = new IxigoFlightsHomePage();
        IxigoLoginPage loginPage = new IxigoLoginPage();

        logger.step("Step 1: Open ixigo flights and log in");
        //homePage.openFlightsHome(flightsUrl);
        loginPage.loginWithMobileOtp(mobile);

        logger.step("Step 2: Search flights");
        homePage.waitForSearchForm();
        homePage.searchFlights(fromQuery, fromAirport, toQuery, toAirport, departDate);

        logger.step("Step 3: Select first flight");
        IxigoFlightResultsPage resultsPage = new IxigoFlightResultsPage();
        Assert.assertTrue(resultsPage.hasFlights(),
                "No flights found for " + fromAirport + " → " + toAirport);
        resultsPage.selectFirstFlight();

        logger.step("Step 4: Review fare and continue");
        IxigoFlightReviewPage reviewPage = new IxigoFlightReviewPage();
        reviewPage.waitForReviewPage();
        reviewPage.clickContinue();

        loginPage.completeLoginIfPrompted(mobile);

        logger.step("Step 5: Traveller / contact details");
        IxigoTravellerDetailsPage travellerPage = new IxigoTravellerDetailsPage();
        travellerPage.fillTravellerDetails(passengerName, mobile, email);
        travellerPage.continueToPayment();

        loginPage.completeLoginIfPrompted(mobile);

        logger.step("Step 6: Verify payment screen");
        IxigoPaymentPage paymentPage = new IxigoPaymentPage();
        paymentPage.waitForPaymentScreen();
        Assert.assertTrue(paymentPage.isPaymentScreenDisplayed(),
                "Payment screen should be visible");
        logger.info("Payment page title/heading: " + paymentPage.getPaymentHeading());

        logger.info("=== Live ixigo Flight Booking Test PASSED ===");
    }

    @DataProvider(name = "flightBookingData")
    public Object[][] flightBookingData() {
        return ExcelUtil.getDataAsArray(
                ConfigReader.getTestDataPath(),
                "FlightBookingData"
        );
    }
}
