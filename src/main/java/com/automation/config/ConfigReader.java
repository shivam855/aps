package com.automation.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties
 * Singleton pattern ensures one instance loads properties once.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/main/resources/config.properties";

    // Static block - loads properties when class is first used
    static {
        try {
            FileInputStream fis = new FileInputStream(CONFIG_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties: " + e.getMessage());
        }
    }

    /** Get any property by key */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value.trim();
    }

    /** Get property or return default when key is missing */
    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value.trim() : defaultValue;
    }

    public static String getBrowser()          { return getProperty("browser"); }
    public static String getBaseUrl()          { return getProperty("base.url"); }
    public static String getTrainsUrl()        { return getProperty("trains.url", getBaseUrl()); }
    public static String getFlightsUrl()       { return getProperty("flights.url", "https://www.ixigo.com/flights"); }
    public static String getFlightMockPagePath() {
        return getProperty("mock.flight.page.path", "src/test/resources/mock/flight-booking-mock.html");
    }
    public static int    getIxigoOtpWaitSeconds() { return Integer.parseInt(getProperty("ixigo.otp.wait.seconds", "90")); }
    public static boolean isSkipLogin()          { return Boolean.parseBoolean(getProperty("ixigo.skip.login", "false")); }
    public static String getIxigoOriginQuery() { return getProperty("ixigo.origin.query"); }
    public static String getIxigoOriginStation() { return getProperty("ixigo.origin.station"); }
    public static String getIxigoDestinationQuery() { return getProperty("ixigo.destination.query"); }
    public static String getIxigoDestinationStation() { return getProperty("ixigo.destination.station"); }
    public static String getIxigoJourneyDay()  { return getProperty("ixigo.journey.day"); }
    public static String getIxigoJourneyDate() { return getProperty("ixigo.journey.date"); }
    public static String getIxigoJourneyQuickChip() { return getProperty("ixigo.journey.quick", "skip"); }
    public static String getIxigoExpectedResultsHeading() { return getProperty("ixigo.results.heading"); }
    public static int    getImplicitWait()     { return Integer.parseInt(getProperty("implicit.wait")); }
    public static int    getExplicitWait()     { return Integer.parseInt(getProperty("explicit.wait")); }
    public static int    getPageLoadTimeout()  { return Integer.parseInt(getProperty("page.load.timeout")); }
    public static String getTestDataPath()     { return getProperty("test.data.path"); }
    public static String getScreenshotPath()   { return getProperty("screenshot.path"); }
    public static String getReportPath()       { return getProperty("report.path"); }
    public static boolean isScreenshotOnFail() { return Boolean.parseBoolean(getProperty("screenshot.on.failure")); }
    public static boolean useMockPage()        { return Boolean.parseBoolean(getProperty("use.mock.page")); }
    public static String getLogLevel()         { return getProperty("log.level"); }
    public static boolean isMockApiEnabled()   { return Boolean.parseBoolean(getProperty("mock.api.enabled")); }
    public static int    getMockApiPort()      { return Integer.parseInt(getProperty("mock.api.port")); }
    public static String getMockApiBaseUrl()   { return getProperty("mock.api.base.url"); }

    /** Resolves mock HTML file to a file:// URL for Selenium navigation */
    public static String getMockPageUrl() {
        String path = getProperty("mock.page.path");
        File mockFile = new File(path);
        if (!mockFile.exists()) {
            throw new RuntimeException("Mock page not found at: " + mockFile.getAbsolutePath());
        }
        return mockFile.toURI().toString();
    }

    /** URL used at test startup — mock page or live base URL */
    public static String getStartUrl() {
        return useMockPage() ? getMockPageUrl() : getBaseUrl();
    }

    /** Week 3 flight booking — mock HTML or live ixigo flights */
    public static String getFlightBookingStartUrl() {
        if (useMockPage()) {
            String path = getFlightMockPagePath();
            File mockFile = new File(path);
            if (!mockFile.exists()) {
                throw new RuntimeException("Flight mock page not found at: " + mockFile.getAbsolutePath());
            }
            return mockFile.toURI().toString();
        }
        return getFlightsUrl();
    }
}
