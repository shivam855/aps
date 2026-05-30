package com.automation.utils;

import com.automation.base.DriverManager;
import com.automation.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil - Captures screenshots on test failure.
 * Screenshots saved to: reports/screenshots/
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    public static String takeScreenshot(String testName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = ConfigReader.getScreenshotPath() + fileName;

        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot saved: " + filePath);
        } catch (IOException e) {
            log.error("Could not save screenshot: " + e.getMessage());
        }
        return filePath;
    }
}
