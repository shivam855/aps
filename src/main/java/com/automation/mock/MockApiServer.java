package com.automation.mock;

import com.automation.config.ConfigReader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lightweight embedded HTTP server that serves mock login, dashboard, and train-search API responses.
 */
public class MockApiServer {

    private static final Logger log = LogManager.getLogger(MockApiServer.class);
    private static final String API_RESOURCE_DIR = "src/test/resources/mock/api";

    private static HttpServer server;
    private static int port;

    private MockApiServer() {
    }

    public static synchronized void start() {
        if (!ConfigReader.isMockApiEnabled()) {
            return;
        }
        if (server != null) {
            return;
        }

        try {
            port = ConfigReader.getMockApiPort();
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
            server.createContext("/mock/api/v1/auth/login", new LoginHandler());
            server.createContext("/mock/api/v1/dashboard", exchange -> serveJson(exchange, "dashboard.json"));
            server.createContext("/mock/api/v1/trains/search", exchange -> serveJson(exchange, "trains-search.json"));
            server.setExecutor(null);
            server.start();
            log.info("Mock API server started at " + ConfigReader.getMockApiBaseUrl());
        } catch (IOException e) {
            throw new RuntimeException("Failed to start mock API server: " + e.getMessage(), e);
        }
    }

    public static synchronized void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            log.info("Mock API server stopped");
        }
    }

    public static int getPort() {
        return port;
    }

    private static void serveJson(HttpExchange exchange, String fileName) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendOptions(exchange);
            return;
        }

        log.info("Mock API hit: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
        byte[] body = readResource(fileName);
        sendJson(exchange, 200, body);
    }

    private static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendOptions(exchange);
                return;
            }

            log.info("Mock API hit: " + exchange.getRequestMethod() + " " + exchange.getRequestURI());

            String requestBody = readRequestBody(exchange);
            boolean valid = requestBody.contains("admin@example.com") && requestBody.contains("Admin@123");

            byte[] body = readResource(valid ? "login-success.json" : "login-error.json");
            sendJson(exchange, valid ? 200 : 401, body);
        }
    }

    private static void sendOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private static void sendJson(HttpExchange exchange, int status, byte[] body) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private static byte[] readResource(String fileName) throws IOException {
        Path path = Paths.get(API_RESOURCE_DIR, fileName);
        if (!Files.exists(path)) {
            throw new IOException("Mock API file not found: " + path.toAbsolutePath());
        }
        return Files.readAllBytes(path);
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
