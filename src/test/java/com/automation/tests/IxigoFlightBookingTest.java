package com.automation.tests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.pages.IxigoFlightBookingPage;
import com.automation.utils.ExcelUtil;
import com.automation.utils.LoggerUtil;

import java.util.Map;

public class IxigoFlightBookingTest extends BaseTest {

    // --------------------------------------------------------
    // Test 1: Search flights on Ixigo and complete till payment with credit card
    // --------------------------------------------------------

    private final LoggerUtil logger = LoggerUtil.getLogger(IxigoFlightBookingTest.class);

    @SuppressWarnings("unchecked")
    @Test(
    		dataProvider = "flightBookingData",
    		description = "Search flights on Ixigo and complete till payment with credit card"
    	)

        public void testIxigoFlow(Map<String, String> testData) throws InterruptedException {
    	
        String MOBILE_NUMBER = testData.get("Mobile");
        String FROM_CITY =  testData.get("FromCity");
        String TO_CITY =  testData.get("ToCity");
        String FIRST_NAME =  testData.get("firstName");
        String LAST_NAME =  testData.get("lastName");
        String CARD_NUMBER =  testData.get("cardNumber");
        String CARD_EXPIRY =  testData.get("exp");
        String CARD_CVV =  testData.get("cvv");
        
    	log.info("Starting: IxigoFlightBookingTest");
    	IxigoFlightBookingPage flightPage = new IxigoFlightBookingPage();
    	log.info("=== Entering Mobioe number ====");
    	flightPage.loginWithMobile(MOBILE_NUMBER);
    	log.info("=== Enter Details ===");
    	flightPage.searchOneWayFlight(FROM_CITY, TO_CITY, IxigoFlightBookingPage.JULY_10_2026_BUTTON);
    	flightPage.proceedToBooking();
    	log.info("== Enter passenger details ===");
    	flightPage.fillTravellerDetails(FIRST_NAME, LAST_NAME);
    	flightPage.continueToPayment();
    	log.info("=== Enter credit card details === ");
    	flightPage.addCardAndPay(CARD_NUMBER, CARD_EXPIRY, CARD_CVV);
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
