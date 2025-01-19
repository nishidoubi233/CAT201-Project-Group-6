import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONObject;

public class RegisterHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(RegisterHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try {
            // 读取请求体
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonData = new JSONObject(requestBody);
            
            // 获取注册信息
            String username = jsonData.getString("username");
            String password = jsonData.getString("password");
            String email = jsonData.getString("email");

            // 验证输入
            if (!validateInput(username, password, email)) {
                sendResponse(exchange, 400, "Invalid input data");
                return;
            }

            // 注册用户
            if (registerUser(username, password, email)) {
                LOGGER.info("User registered successfully: " + username);
                sendResponse(exchange, 200, "Registration successful");
            } else {
                LOGGER.warning("Registration failed for username: " + username);
                sendResponse(exchange, 500, "Registration failed");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Registration error", e);
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private boolean validateInput(String username, String password, String email) {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during registration", e);
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
