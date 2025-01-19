import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // 检查是否是 POST 请求
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // 读取请求体中的数据
            InputStream requestBody = exchange.getRequestBody();
            String requestData = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            
            // 将 JSON 字符串解析为 Map
            Map<String, String> jsonData = parseJson(requestData);
            
            // 获取注册信息
            String username = jsonData.get("username");
            String password = jsonData.get("password");
            String email = jsonData.get("email");

            // 验证输入
            if (username == null || password == null || email == null) {
                sendResponse(exchange, 400, "All fields are required");
                return;
            }

            // 保存到数据库
            if (registerUser(username, password, email)) {
                sendResponse(exchange, 200, "Registration successful");
            } else {
                sendResponse(exchange, 500, "Registration failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * 解析简单的 JSON 字符串为 Map
     * 注意：这是一个简单的实现，只适用于简单的 JSON 格式
     */
    private Map<String, String> parseJson(String json) {
        Map<String, String> result = new HashMap<>();
        // 移除 {} 和空格
        json = json.trim().replaceAll("^\\{|\\}$", "");
        // 分割键值对
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");
                result.put(key, value);
            }
        }
        return result;
    }

    /**
     * 将用户信息注册到数据库
     * @return 注册成功返回 true，失败返回 false
     */
    private boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送 HTTP 响应
     * @param statusCode HTTP 状态码
     * @param message 响应消息
     */
    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String response = String.format("{\"status\":\"%s\",\"message\":\"%s\"}", 
            statusCode == 200 ? "success" : "error", 
            message);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, response.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
