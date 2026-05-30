package com.automation.mock;

import com.automation.base.DriverManager;
import com.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Map;

/**
 * Redirects browser fetch/XHR calls from real ixigo API hosts to the local mock server.
 * Injected before page load via Chrome DevTools Protocol.
 */
public class BrowserApiMock {

    private static final Logger log = LogManager.getLogger(BrowserApiMock.class);

    private static final String[] MOCK_HOSTS = {
            "ixigotrainsapi.confirmtkt.com"
    };

    private BrowserApiMock() {
    }

    public static void enableIfConfigured() {
        if (!ConfigReader.isMockApiEnabled()) {
            return;
        }

        WebDriver driver = DriverManager.getDriver();
        if (!(driver instanceof ChromeDriver)) {
            log.warn("Browser API mock requires ChromeDriver; skipping injection");
            return;
        }

        String script = buildRedirectScript(ConfigReader.getMockApiBaseUrl());
        try {
            ((ChromeDriver) driver).executeCdpCommand(
                    "Page.addScriptToEvaluateOnNewDocument",
                    Map.of("source", script));
            log.info("Browser API mock interceptor injected (real APIs will route to mock server)");
        } catch (Exception e) {
            log.warn("Could not inject API mock via CDP: " + e.getMessage());
        }
    }

    static String buildRedirectScript(String mockBaseUrl) {
        String hostsJson = java.util.Arrays.stream(MOCK_HOSTS)
                .map(h -> "\"" + h + "\"")
                .collect(java.util.stream.Collectors.joining(","));

        return ""
                + "(() => {"
                + "  const MOCK_BASE = '" + mockBaseUrl + "';"
                + "  const REAL_HOSTS = [" + hostsJson + "];"
                + "  const API_PATHS = ['/api/v1/auth/login', '/api/v1/dashboard', '/api/v1/trains/search'];"
                + "  function toMockUrl(url) {"
                + "    try {"
                + "      const u = new URL(url, window.location.href);"
                + "      if (!REAL_HOSTS.includes(u.hostname)) return null;"
                + "      if (!API_PATHS.some(p => u.pathname.includes(p))) return null;"
                + "      return MOCK_BASE + '/mock' + u.pathname + u.search;"
                + "    } catch (e) { return null; }"
                + "  }"
                + "  const origFetch = window.fetch;"
                + "  window.fetch = function(input, init) {"
                + "    const url = typeof input === 'string' ? input : input.url;"
                + "    const mockUrl = toMockUrl(url);"
                + "    if (mockUrl) { console.log('[MockAPI] fetch redirected:', url, '->', mockUrl); return origFetch(mockUrl, init); }"
                + "    return origFetch.apply(this, arguments);"
                + "  };"
                + "  const origOpen = XMLHttpRequest.prototype.open;"
                + "  XMLHttpRequest.prototype.open = function(method, url) {"
                + "    const mockUrl = toMockUrl(url);"
                + "    if (mockUrl) { console.log('[MockAPI] XHR redirected:', url, '->', mockUrl); return origOpen.apply(this, [method, mockUrl].concat([].slice.call(arguments, 2))); }"
                + "    return origOpen.apply(this, arguments);"
                + "  };"
                + "})();";
    }
}
