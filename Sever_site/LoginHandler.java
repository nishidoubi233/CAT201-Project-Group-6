import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import org.json.*;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 设置CORS头
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // 处理OPTIONS请求
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // 读取请求体
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    requestBody.append(line);
                }

                // 解析JSON
                JSONObject jsonRequest = new JSONObject(requestBody.toString());
                String username = jsonRequest.getString("username");
                String password = jsonRequest.getString("password");

                // 验证用户
                JSONObject response = authenticateUser(username, password);
                
                // 设置响应头
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                
                // 发送响应
                byte[] responseBytes = response.toString().getBytes("UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();

            } catch (Exception e) {
                String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(500, errorMessage.length());
                OutputStream os = exchange.getResponseBody();
                os.write(errorMessage.getBytes());
                os.close();
            }
        } else {
            String response = "{\"error\": \"Method not allowed\"}";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private JSONObject authenticateUser(String username, String password) throws Exception {
        JSONObject response = new JSONObject();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                response.put("status", "error");
                response.put("message", "User does not exist!");
                return response;
            }

            String storedPassword = rs.getString("password");
            if (!PasswordHasher.verify(password, storedPassword)) {
                response.put("status", "error");
                response.put("message", "Incorrect password!");
                return response;
            }

            response.put("status", "success");
            response.put("message", "Login successful!");
            
            JSONObject user = new JSONObject();
            user.put("id", rs.getInt("id"));
            user.put("username", rs.getString("username"));
            user.put("email", rs.getString("email"));
            response.put("user", user);

        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage());
        }
        
        return response;
    }
}
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.sql.*;
import org.json.*;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 设置CORS头
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // 处理OPTIONS请求
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // 读取请求体
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    requestBody.append(line);
                }

                // 解析JSON
                JSONObject jsonRequest = new JSONObject(requestBody.toString());
                String username = jsonRequest.getString("username");
                String password = jsonRequest.getString("password");

                // 验证用户
                JSONObject response = authenticateUser(username, password);
                
                // 设置响应头
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                
                // 发送响应
                byte[] responseBytes = response.toString().getBytes("UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();

            } catch (Exception e) {
                String errorMessage = "{\"error\": \"" + e.getMessage() + "\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(500, errorMessage.length());
                OutputStream os = exchange.getResponseBody();
                os.write(errorMessage.getBytes());
                os.close();
            }
        } else {
            String response = "{\"error\": \"Method not allowed\"}";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private JSONObject authenticateUser(String username, String password) throws Exception {
        JSONObject response = new JSONObject();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                response.put("status", "error");
                response.put("message", "User does not exist!");
                return response;
            }

            String storedPassword = rs.getString("password");
            if (!PasswordHasher.verify(password, storedPassword)) {
                response.put("status", "error");
                response.put("message", "Incorrect password!");
                return response;
            }

            response.put("status", "success");
            response.put("message", "Login successful!");
            
            JSONObject user = new JSONObject();
            user.put("id", rs.getInt("id"));
            user.put("username", rs.getString("username"));
            user.put("email", rs.getString("email"));
            response.put("user", user);

        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage());
        }
        
        return response;
    }
}
