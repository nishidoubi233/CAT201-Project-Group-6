import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(RegisterHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            LOGGER.info("Received registration request: " + requestBody);
            Map<String, String> jsonData = parseJson(requestBody);
            
            String username = jsonData.get("username");
            String password = jsonData.get("password");
            String email = jsonData.get("email");

            if (!validateInput(username, password, email)) {
                LOGGER.warning("Invalid input data for registration");
                sendResponse(exchange, 400, "请填写所有必填字段，并确保邮箱格式正确");
                return;
            }

            if (isUserExists(username, email)) {
                LOGGER.warning("Username or email already exists: " + username);
                sendResponse(exchange, 409, "用户名或邮箱已被注册");
                return;
            }

            if (registerUser(username, password, email)) {
                LOGGER.info("User registered successfully: " + username);
                sendResponse(exchange, 200, "注册成功");
            } else {
                LOGGER.warning("Registration failed for username: " + username);
                sendResponse(exchange, 500, "注册失败，请稍后重试");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration error", e);
            sendResponse(exchange, 500, "服务器错误：" + e.getMessage());
        }
    }

    private boolean isUserExists(String username, String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error checking user existence", e);
        }
        return false;
    }

    private boolean validateInput(String username, String password, String email) {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during registration", e);
            return false;
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
}
