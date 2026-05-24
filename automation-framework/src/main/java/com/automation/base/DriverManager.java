package com.automation.base;

import com.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

/**
 * DriverManager - Manages WebDriver lifecycle.
 * Uses ThreadLocal for parallel test execution safety.
 */
public class DriverManager {

    // ThreadLocal ensures each thread (test) gets its own WebDriver instance
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Initialize WebDriver based on browser in config.properties
     */
    public static void initDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        WebDriver webDriver;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                // options.addArguments("--headless");  // Uncomment for headless mode
                options.addArguments("--start-maximized");
                options.addArguments("--disable-notifications");
                webDriver = new ChromeDriver(options);
                break;
        }

        // Set timeouts
        webDriver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()))
                .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

        webDriver.manage().window().maximize();

        driver.set(webDriver);
    }

    /** Get the current thread's WebDriver */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            throw new RuntimeException("WebDriver not initialized. Call initDriver() first.");
        }
        return driver.get();
    }

    /** Quit browser and remove driver from ThreadLocal */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
