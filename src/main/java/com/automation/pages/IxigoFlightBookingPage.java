package com.automation.pages;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class IxigoFlightBookingPage  extends BasePage {

    public IxigoFlightBookingPage() {
    }
    
    private static final long LOGIN_SETTLE_WAIT_MS = 10000;

    public static final By LOGIN_SIGNUP_BUTTON = By.xpath("(//button[text()='Log in/Sign up'])[1]");
    public static final By MOBILE_NUMBER_INPUT = By.xpath("//input[@aria-label='Enter Mobile Number' or @placeholder='Enter Mobile Number']");
    public static final By CONTINUE_BUTTON = By.xpath("//button[normalize-space()='Continue']");
    public static final By IXIGO_FLIGHTS_LINK = By.xpath("//a/p[text()='Flights']");
    public static final By ONE_WAY_TAB = By.xpath("//*[@role='tab' and normalize-space()='One Way']");

    public static final By FROM_SECTION = By.xpath("//div[@class=\"flex justify-between items-center relative w-full h-full  block\"]");
    public static final By FROM_TEXT_INPUT = By.xpath("(//div/input)[1]");
    public static final By TO_TEXT_INPUT = By.xpath("(//div/input)[2]");
    public static final By NEW_DELHI_OPTION = By.xpath("(//p[contains(normalize-space(),'New Delhi, Delhi, India')])[1]");
    public static final By KOLKATA_OPTION = By.xpath("(//p[contains(normalize-space(),'Kolkata, West Bengal, India')])[1]");

    public static final By DEPARTURE_DATE = By.cssSelector("[data-testid='departureDate']");
    public static final By JULY_10_2026_BUTTON = By.xpath("(//button/*[@aria-label=\"July 10, 2026\"])[last()]");
    public static final By PAX_DROPDOWN = By.cssSelector("[data-testid='pax']");
    public static final By PAX_COUNT_ONE = By.xpath("(//button[@data-testid='1'])[1]");
    public static final By DONE_BTN = By.xpath("//button[text()='Done']");
    public static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    public static final By BOOK_BUTTON = By.xpath("//button[normalize-space()='Book']");
    public static final By NO_FREE_RADIO = By.xpath("//input[@id=\"standalone-none-fareType\"]");
    public static final By TITLE_DROPDOWN = By.xpath("//label[text()='Title']/following::input[1]");
    public static final By MR_TITLE = By.xpath("//*[normalize-space()='Mr']");
    public static final By FIRST_NAME_INPUT = By.xpath("//label[contains(text(),'First')]/following::input[1]");
    public static final By LAST_NAME_INPUT = By.xpath("//label[contains(text(),'Last Name')]/following::input[1]");
    public static final By ADULT_COUNTRY_INPUT = By.xpath("//*[@id='adult1']//input[@aria-label='Country' or @placeholder='Country']");
    public static final By INDIA_COUNTRY_CODE = By.xpath("//div[@id=\"portal-root\"]//p[text()='India']");

    public static final By CONFIRM_BUTTON = By.xpath("//button[normalize-space()='Confirm']");
    public static final By NO_THANKS_BUTTON = By.xpath("//button[normalize-space()='No, Thanks']");
    public static final By ADD_ON_TILE = By.cssSelector("div:nth-child(7) > div:nth-child(7) > .cursor-pointer");
    public static final By MEAL_SELECTION_BUTTON = By.xpath("//button[normalize-space()='Meal Selection']");
    public static final By CONTINUE_TO_PAY_BUTTON = By.xpath("//button[contains(normalize-space(),'Continue To Pay')]");

    public static final By CARD_PAYMENT_OPTION = By.xpath("//p[text()='Credit/Debit/ATM Card']");
    public static final By ADD_NEW_CARD = By.xpath("//p[text()='Add New Card']");
    public static final By CARD_NUMBER = By.cssSelector("[data-testid='card-number']");
    public static final By CARD_EXP_DATE = By.cssSelector("[data-testid='card-exp-date']");
    public static final By NEW_CARD_CVV = By.cssSelector("[data-testid='new-card-cvv']");
    public static final By SECURELY_PAY_BUTTON = By.xpath("//button[contains(normalize-space(),'Securely Pay')]");
    
    public void loginWithMobile(String mobileNumber) {
        click(IxigoFlightBookingPage.LOGIN_SIGNUP_BUTTON);
        type(IxigoFlightBookingPage.MOBILE_NUMBER_INPUT, mobileNumber);
        click(IxigoFlightBookingPage.CONTINUE_BUTTON);
        sleep(LOGIN_SETTLE_WAIT_MS);
        sleep(LOGIN_SETTLE_WAIT_MS);
    }

    public void searchOneWayFlight(String fromCity, String toCity, By departureDateOption) {
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

    public void proceedToBooking() {
        click(IxigoFlightBookingPage.BOOK_BUTTON);
        scrollIntoViewAndClick(IxigoFlightBookingPage.NO_FREE_RADIO);
    }

    public void fillTravellerDetails(String firstName, String lastName) {
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

    public void continueToPayment() throws InterruptedException {
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

    public void addCardAndPay(String cardNumber, String expiryDate, String cvv) {
        click(IxigoFlightBookingPage.CARD_PAYMENT_OPTION);
        click(IxigoFlightBookingPage.ADD_NEW_CARD);

        type(IxigoFlightBookingPage.CARD_NUMBER, cardNumber);
        type(IxigoFlightBookingPage.CARD_EXP_DATE, expiryDate);
        type(IxigoFlightBookingPage.NEW_CARD_CVV, cvv);

        click(IxigoFlightBookingPage.SECURELY_PAY_BUTTON);
    }

    protected void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    protected void type(By by, String value) {
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

