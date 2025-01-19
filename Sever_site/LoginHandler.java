import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * 处理用户登录请求的处理器
 * 验证用户名和密码，返回登录结果
 */
public class LoginHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(LoginHandler.class.getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 设置CORS头，允许跨域请求
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // 处理 OPTIONS 预检请求
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // 读取并解析请求体
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                
                // 从请求体中提取用户名和密码
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");

                // 验证用户凭据
                if (validateUser(username, password)) {
                    String response = "{\"status\":\"success\",\"message\":\"Login successful\"}";
                    sendResponse(exchange, 200, response);
                } else {
                    String response = "{\"status\":\"error\",\"message\":\"Invalid credentials\"}";
                    sendResponse(exchange, 401, response);
                }
                
            } catch (Exception e) {
                // 记录错误日志
                LOGGER.log(Level.SEVERE, "登录过程中发生错误", e);
                String response = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            // 处理非 POST 请求
            String response = "{\"status\":\"error\",\"message\":\"Method not allowed\"}";
            sendResponse(exchange, 405, response);
        }
    }

    /**
     * 从请求体中提取指定键的值
     * @param requestBody JSON 格式的请求体
     * @param key 要提取的键名
     * @return 提取的值
     */
    private String extractValue(String requestBody, String key) {
        String searchStr = "\"" + key + "\":\"";
        int start = requestBody.indexOf(searchStr) + searchStr.length();
        int end = requestBody.indexOf("\"", start);
        return requestBody.substring(start, end);
    }

    /**
     * 验证用户名和密码是否正确
     * @param username 用户名
     * @param password 密码
     * @return 验证成功返回 true，失败返回 false
     */
    private boolean validateUser(String username, String password) {
        // 验证输入不为空
        if (username == null || password == null || 
            username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        // 查询数据库验证用户
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // 如果有匹配记录则返回 true
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "数据库查询错误", e);
            return false;
        }
    }

    /**
     * 发送 HTTP 响应
     * @param exchange HTTP 交换对象
     * @param statusCode HTTP 状态码
     * @param response 响应内容
     */
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
