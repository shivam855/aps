package com.qa.pages;

import org.openqa.selenium.By;

public final class IxigoFlightBookingPage {

    private IxigoFlightBookingPage() {
    }

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
    public static final By JULY_10_2026_BUTTON = By.xpath("(//button/*[@aria-label=\"10 July 2026\"])[last()]");
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
}

