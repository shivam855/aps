package com.automation.config;

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

    public static String getBrowser()          { return getProperty("browser"); }
    public static String getBaseUrl()          { return getProperty("base.url"); }
    public static int    getImplicitWait()     { return Integer.parseInt(getProperty("implicit.wait")); }
    public static int    getExplicitWait()     { return Integer.parseInt(getProperty("explicit.wait")); }
    public static int    getPageLoadTimeout()  { return Integer.parseInt(getProperty("page.load.timeout")); }
    public static String getTestDataPath()     { return getProperty("test.data.path"); }
    public static String getScreenshotPath()   { return getProperty("screenshot.path"); }
    public static String getReportPath()       { return getProperty("report.path"); }
    public static boolean isScreenshotOnFail() { return Boolean.parseBoolean(getProperty("screenshot.on.failure")); }
}
