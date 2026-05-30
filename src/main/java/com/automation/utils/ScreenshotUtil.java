package com.automation.utils;

import com.automation.base.DriverManager;
import com.automation.config.ConfigReader;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures screenshots to disk and as Base64 for inline Extent report embedding.
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    public static String captureBase64() {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Could not capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Attaches a screenshot to the failing Extent step/test entry.
     * Uses Base64 first (embedded in HTML), then falls back to a relative file path.
     */
    public static boolean attachScreenshotTo(ExtentTest target, String message, String fileLabel) {
        String label = fileLabel != null ? fileLabel : "failure";
        String base64 = captureBase64();
        if (base64 != null && !base64.isEmpty()) {
            try {
                target.fail(message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
                log.info("Screenshot embedded in Extent report (Base64)");
                return true;
            } catch (Exception e) {
                log.warn("Base64 embed failed, trying file path: " + e.getMessage());
            }
        }

        String relativePath = takeScreenshot(label);
        if (relativePath != null) {
            try {
                target.fail(message,
                        MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
                log.info("Screenshot attached in Extent report: " + relativePath);
                return true;
            } catch (Exception e) {
                log.error("Could not attach screenshot from path: " + e.getMessage());
            }
        }

        target.fail(message);
        return false;
    }

    /** Saves screenshot to disk; returns path relative to the HTML report */
    public static String takeScreenshot(String testName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = testName + "_" + timestamp + ".png";
        String relativePath = "screenshots/" + fileName;
        String absolutePath = ConfigReader.getScreenshotPath() + fileName;

        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File(absolutePath);
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot saved: " + absolutePath);
            return relativePath;
        } catch (Exception e) {
            log.error("Could not save screenshot: " + e.getMessage());
            return null;
        }
    }
}
