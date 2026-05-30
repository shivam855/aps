package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Example Page Object using @FindBy annotations.
 *
 * HOW TO CREATE A NEW PAGE:
 * 1. Extend BasePage
 * 2. Define WebElements using @FindBy
 * 3. Create action methods that use BasePage helper methods
 */
public class LoginPage extends BasePage {

    // ----- WebElements (located via @FindBy) -----

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    // ----- Page Actions -----

    public void enterUsername(String username) {
        type(usernameField, username);
        log.info("Entered username: " + username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
        log.info("Entered password");
    }

    public void clickLogin() {
        click(loginButton);
        log.info("Clicked Login button");
    }

    /** Convenience method - perform full login in one call */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
