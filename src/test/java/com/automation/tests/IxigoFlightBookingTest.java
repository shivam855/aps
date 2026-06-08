import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.automation.pages.IxigoFlightBookingPage;

import java.time.Duration;
import java.util.List;

public class IxigoFlightBookingTest {

    private static final String IXIGO_FLIGHTS_URL = "https://www.ixigo.com/flights";
    private static final String MOBILE_NUMBER = "9667525275";
    private static final String FROM_CITY = "New Delhi";
    private static final String TO_CITY = "Kolkata";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "Account";
    private static final String CARD_NUMBER = "4111 1111 1111 1111";
    private static final String CARD_EXPIRY = "11/28";
    private static final String CARD_CVV = "123";
    private static final long LOGIN_SETTLE_WAIT_MS = 10000;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testIxigoFlow() throws InterruptedException {
        openIxigoFlightsPage();
        loginWithMobile(MOBILE_NUMBER);
        searchOneWayFlight(FROM_CITY, TO_CITY, IxigoFlightBookingPage.JULY_10_2026_BUTTON);
        proceedToBooking();
        fillTravellerDetails(FIRST_NAME, LAST_NAME);
        continueToPayment();
        addCardAndPay(CARD_NUMBER, CARD_EXPIRY, CARD_CVV);
    }

    private void openIxigoFlightsPage() {
        driver.get(IXIGO_FLIGHTS_URL);
    }

    private void loginWithMobile(String mobileNumber) {
        click(IxigoFlightBookingPage.LOGIN_SIGNUP_BUTTON);
        type(IxigoFlightBookingPage.MOBILE_NUMBER_INPUT, mobileNumber);
        click(IxigoFlightBookingPage.CONTINUE_BUTTON);
        sleep(LOGIN_SETTLE_WAIT_MS);
        sleep(LOGIN_SETTLE_WAIT_MS);
    }

    private void searchOneWayFlight(String fromCity, String toCity, By departureDateOption) {
        clickNthSafe(IxigoFlightBookingPage.IXIGO_FLIGHTS_LINK, 1);
        click(IxigoFlightBookingPage.ONE_WAY_TAB);

        click(IxigoFlightBookingPage.FROM_SECTION);
        type(IxigoFlightBookingPage.FROM_TEXT_INPUT, fromCity);
        click(IxigoFlightBookingPage.NEW_DELHI_OPTION);

        type(IxigoFlightBookingPage.TO_TEXT_INPUT, toCity);
        click(IxigoFlightBookingPage.KOLKATA_OPTION);

        click(IxigoFlightBookingPage.DEPARTURE_DATE);
        click(departureDateOption);

        clickAll(
                IxigoFlightBookingPage.PAX_DROPDOWN,
                IxigoFlightBookingPage.PAX_COUNT_ONE,
                IxigoFlightBookingPage.DONE_BTN,
                IxigoFlightBookingPage.SEARCH_BUTTON
        );
    }

    private void proceedToBooking() {
        click(IxigoFlightBookingPage.BOOK_BUTTON);
        scrollIntoViewAndClick(IxigoFlightBookingPage.NO_FREE_RADIO);
    }

    private void fillTravellerDetails(String firstName, String lastName) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(IxigoFlightBookingPage.TITLE_DROPDOWN));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true); window.scrollBy(0, -120);",
                element
        );
        click(IxigoFlightBookingPage.TITLE_DROPDOWN);
        click(IxigoFlightBookingPage.MR_TITLE);

        type(IxigoFlightBookingPage.FIRST_NAME_INPUT, firstName);
        type(IxigoFlightBookingPage.LAST_NAME_INPUT, lastName);

        click(IxigoFlightBookingPage.ADULT_COUNTRY_INPUT);
        click(IxigoFlightBookingPage.INDIA_COUNTRY_CODE);
    }

    private void continueToPayment() throws InterruptedException {
        clickAll(
                IxigoFlightBookingPage.CONTINUE_BUTTON,
                IxigoFlightBookingPage.CONFIRM_BUTTON,
                IxigoFlightBookingPage.NO_THANKS_BUTTON
        );
        Thread.sleep(10000);
        clickAll(
                IxigoFlightBookingPage.ADD_ON_TILE,
                IxigoFlightBookingPage.MEAL_SELECTION_BUTTON,
                IxigoFlightBookingPage.CONTINUE_BUTTON,
                IxigoFlightBookingPage.CONTINUE_BUTTON,
                IxigoFlightBookingPage.CONTINUE_TO_PAY_BUTTON
        );
    }

    private void addCardAndPay(String cardNumber, String expiryDate, String cvv) {
        click(IxigoFlightBookingPage.CARD_PAYMENT_OPTION);
        click(IxigoFlightBookingPage.ADD_NEW_CARD);

        type(IxigoFlightBookingPage.CARD_NUMBER, cardNumber);
        type(IxigoFlightBookingPage.CARD_EXP_DATE, expiryDate);
        type(IxigoFlightBookingPage.NEW_CARD_CVV, cvv);

        click(IxigoFlightBookingPage.SECURELY_PAY_BUTTON);
    }

    private void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    private void type(By by, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        el.clear();
        el.sendKeys(value);
    }

    private void clickAll(By... locators) {
        for (By locator : locators) {
            click(locator);
        }
    }

    private void scrollIntoViewAndClick(By by) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true); window.scrollBy(0, -120);",
                element
        );
        element.click();
    }

    private void clickNth(By by, int index) {
        wait.until(driver -> {
            List<WebElement> elements = driver.findElements(by);
            return elements.size() > index ? elements : null;
        });
        List<WebElement> elements = driver.findElements(by);
        wait.until(ExpectedConditions.elementToBeClickable(elements.get(index))).click();
    }

    private void clickNthSafe(By by, int index) {
        try {
            clickNth(by, index);
        } catch (ElementClickInterceptedException ignored) {
            // If a transient overlay intercepts this optional navigation click, proceed with next step.
        }
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
