import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONObject;

public class LoginHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method not allowed");
            return;
        }

        try {
            // 读取请求体
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonData = new JSONObject(requestBody);
            
            String username = jsonData.getString("username");
            String password = jsonData.getString("password");

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

    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        JSONObject response = new JSONObject();
        response.put("status", statusCode == 200 ? "success" : "error");
        response.put("message", message);
        
        String responseBody = response.toString();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBody.getBytes(StandardCharsets.UTF_8).length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBody.getBytes(StandardCharsets.UTF_8));
        }
    }
}

要使用这些代码，你需要：