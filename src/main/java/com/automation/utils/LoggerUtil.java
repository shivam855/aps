package com.automation.utils;

import com.automation.config.ConfigReader;
import com.automation.reporting.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Framework logging utility with three semantic levels: INFO, DEBUG, FAIL.
 * FAIL always captures a screenshot on the active Extent step.
 */
public class LoggerUtil {

    private final Logger logger;

    private LoggerUtil(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    public static LoggerUtil getLogger(Class<?> clazz) {
        return new LoggerUtil(clazz);
    }

    /** Opens an Extent step node — failures inside this step attach screenshots here */
    public void step(String stepName) {
        logger.info(stepName);
        ExtentReportManager.startStep(stepName);
    }

    /** General execution milestones and step summaries */
    public void info(String message) {
        logger.info(message);
        logToExtent(message, LogLevel.INFO);
    }

    /** Detailed diagnostic output for troubleshooting */
    public void debug(String message) {
        logger.debug(message);
        logToExtent(message, LogLevel.DEBUG);
    }

    /** Test or step failure — screenshot attached at the active step */
    public void fail(String message) {
        logger.error("[FAIL] " + message);
        if (ConfigReader.isScreenshotOnFail()) {
            ExtentReportManager.failWithScreenshot(message, null);
        } else {
            ExtentTest target = ExtentReportManager.getActiveTarget();
            if (target != null) {
                target.fail(message);
            }
        }
    }

    public void fail(String message, Throwable throwable) {
        logger.error("[FAIL] " + message, throwable);
        if (!ConfigReader.isScreenshotOnFail()) {
            ExtentTest target = ExtentReportManager.getActiveTarget();
            if (target != null) {
                target.fail(message);
                if (throwable != null) {
                    target.fail(throwable);
                }
            }
        }
    }

    private void logToExtent(String message, LogLevel level) {
        ExtentTest target = ExtentReportManager.getActiveTarget();
        if (target == null) {
            return;
        }
        switch (level) {
            case INFO:
                target.info(message);
                break;
            case DEBUG:
                target.info("<b>[DEBUG]</b> " + message);
                break;
            case FAIL:
                ExtentReportManager.failWithScreenshot(message, null);
                break;
            default:
                break;
        }
    }

    private enum LogLevel {
        INFO, DEBUG, FAIL
    }
}
