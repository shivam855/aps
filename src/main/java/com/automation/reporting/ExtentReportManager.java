package com.automation.reporting;

import com.automation.config.ConfigReader;
import com.automation.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Singleton manager for ExtentReports HTML output.
 * Supports step nodes so failures attach screenshots at the active step.
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> currentStep = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized void initReport() {
        if (extentReports != null) {
            return;
        }

        String reportPath = ConfigReader.getReportPath();
        File reportFile = new File(reportPath);
        File reportDir = reportFile.getParentFile();
        if (reportDir != null) {
            reportDir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
        sparkReporter.config().setDocumentTitle("Automation Framework Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
        extentReports.setSystemInfo("Environment", ConfigReader.useMockPage() ? "Mock (Local HTML)" : "Live");
    }

    public static void createTest(String testName, String description) {
        if (extentReports == null) {
            initReport();
        }
        String safeDescription = description != null ? description : "";
        extentTest.set(extentReports.createTest(testName, safeDescription));
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /** Active log target — current step node, or the test if no step is open */
    public static ExtentTest getActiveTarget() {
        ExtentTest step = currentStep.get();
        return step != null ? step : extentTest.get();
    }

    /** Opens a new step node; the previous open step is marked passed */
    public static void startStep(String stepName) {
        finishCurrentStep();
        ExtentTest test = getTest();
        if (test != null) {
            currentStep.set(test.createNode(stepName));
        }
    }

    /** Marks the current step as passed and closes it */
    public static void finishCurrentStep() {
        ExtentTest step = currentStep.get();
        if (step != null) {
            step.pass("Step completed");
            currentStep.remove();
        }
    }

    /** Logs a failure with an inline screenshot on the active step (or test) */
    public static void failWithScreenshot(String message, Throwable throwable) {
        ExtentTest target = getActiveTarget();
        if (target == null) {
            return;
        }

        String label = "failure";
        if (target.getModel() != null && target.getModel().getName() != null) {
            label = target.getModel().getName().replaceAll("\\s+", "_");
        }

        if (!ScreenshotUtil.attachScreenshotTo(target, message, label)) {
            if (throwable != null) {
                target.fail(throwable);
            }
        } else if (throwable != null) {
            target.fail(throwable);
        }

        if (currentStep.get() == target) {
            currentStep.remove();
        }
    }

    public static void removeTest() {
        currentStep.remove();
        extentTest.remove();
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
