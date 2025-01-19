import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.info("Received login request");
        
        if (!"POST".equals(exchange.getRequestMethod())) {
            LOGGER.warning("Invalid request method: " + exchange.getRequestMethod());
            sendResponse(exchange, 405, "Method not allowed");
            return;
        }

        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            LOGGER.info("Request body: " + requestBody);
            Map<String, String> jsonData = parseJson(requestBody);
            
            String username = jsonData.get("username");
            String password = jsonData.get("password");
            LOGGER.info("Attempting login for user: " + username);

            if (validateUser(username, password)) {
                LOGGER.info("User logged in successfully: " + username);
                sendResponse(exchange, 200, "Login successful");
            } else {
                LOGGER.warning("Login failed for username: " + username);
                sendResponse(exchange, 401, "Invalid credentials");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login error", e);
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String jsonResponse = String.format(
            "{\"status\":\"%s\",\"message\":\"%s\"}",
            statusCode == 200 ? "success" : "error",
            message.replace("\"", "\\\"")
        );
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }
    }

    private boolean validateUser(String username, String password) {
        if (username == null || password == null || 
            username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            return false;
        }
    }
}

