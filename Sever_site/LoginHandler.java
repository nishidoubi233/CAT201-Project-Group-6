import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoginHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 设置CORS头
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // 读取请求体
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                
                // 使用简单的字符串解析替代 JSONObject
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");

                // 验证用户
                if (validateUser(username, password)) {
                    String response = "{\"status\":\"success\",\"message\":\"Login successful\"}";
                    sendResponse(exchange, 200, response);
                } else {
                    String response = "{\"status\":\"error\",\"message\":\"Invalid credentials\"}";
                    sendResponse(exchange, 401, response);
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during login", e);
                String response = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            String response = "{\"status\":\"error\",\"message\":\"Method not allowed\"}";
            sendResponse(exchange, 405, response);
        }
    }

    private String extractValue(String requestBody, String key) {
        String searchStr = "\"" + key + "\":\"";
        int start = requestBody.indexOf(searchStr) + searchStr.length();
        int end = requestBody.indexOf("\"", start);
        return requestBody.substring(start, end);
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
            LOGGER.log(Level.SEVERE, "Database error", e);
            return false;
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
