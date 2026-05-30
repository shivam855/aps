package com.automation.listeners;

import com.automation.config.ConfigReader;
import com.automation.mock.MockApiServer;
import com.automation.utils.LoggerUtil;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * Starts and stops the embedded mock API server for the test suite.
 */
public class MockApiListener implements ISuiteListener {

    private static final LoggerUtil log = LoggerUtil.getLogger(MockApiListener.class);

    @Override
    public void onStart(ISuite suite) {
        if (ConfigReader.isMockApiEnabled()) {
            log.info("Starting mock API server for suite: " + suite.getName());
            MockApiServer.start();
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        MockApiServer.stop();
    }
}
