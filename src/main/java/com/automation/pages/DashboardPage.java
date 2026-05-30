package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Dashboard page shown after successful mock API login.
 */
public class DashboardPage extends BasePage {

    @FindBy(id = "dashboard")
    private WebElement dashboardPanel;

    @FindBy(id = "welcomeMessage")
    private WebElement welcomeMessage;

    @FindBy(id = "searchSummary")
    private WebElement searchSummary;

    @FindBy(id = "trainResults")
    private WebElement trainResults;

    public boolean isDashboardDisplayed() {
        try {
            return dashboardPanel.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForDashboard() {
        waitForVisible(dashboardPanel);
    }

    public void waitForTrainResults() {
        waitForVisible(By.cssSelector("#trainResults .train-card"));
    }

    public String getWelcomeMessage() {
        return getText(welcomeMessage);
    }

    public String getSearchSummary() {
        return getText(searchSummary);
    }

    public int getTrainResultCount() {
        return driver.findElements(By.cssSelector("#trainResults .train-card")).size();
    }

    public boolean isTrainListed(String trainNumber) {
        return !driver.findElements(By.cssSelector("[data-train-number='" + trainNumber + "']")).isEmpty();
    }
}
