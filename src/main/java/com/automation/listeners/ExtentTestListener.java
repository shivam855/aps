package com.automation.listeners;

import com.automation.reporting.ExtentReportManager;
import com.automation.utils.LoggerUtil;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for ExtentReports suite lifecycle.
 * Screenshot capture and cleanup happen in BaseTest @AfterMethod (after this listener's callbacks).
 */
public class ExtentTestListener implements ISuiteListener, ITestListener {

    private static final LoggerUtil log = LoggerUtil.getLogger(ExtentTestListener.class);

    @Override
    public void onStart(ISuite suite) {
        log.info("Initializing Extent Report for suite: " + suite.getName());
        ExtentReportManager.initReport();
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("Flushing Extent Report");
        ExtentReportManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentReportManager.createTest(result.getMethod().getMethodName(), description);
        log.info("Test started: " + result.getMethod().getMethodName());
    }
}
