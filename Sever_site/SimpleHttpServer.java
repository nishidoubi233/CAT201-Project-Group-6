import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.OutputStream;

public class SimpleHttpServer {
    private static final Logger LOGGER = Logger.getLogger(SimpleHttpServer.class.getName());
    private HttpServer server;
    private static final int START_PORT = 8080;
    private int port;

    public SimpleHttpServer() throws IOException {
        port = START_PORT;
        int maxAttempts = 100;
        int attempts = 0;
        
        while (attempts < maxAttempts) {
            try {
                server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
                LOGGER.info("Successfully created server on port " + port);
                break;
            } catch (IOException e) {
                attempts++;
                port++;
                LOGGER.warning("Port " + (port-1) + " is in use, trying port " + port);
                
                if (attempts >= maxAttempts) {
                    LOGGER.severe("Could not find available port after " + maxAttempts + " attempts");
                    throw new IOException("No available ports found between " + START_PORT + " and " + (START_PORT + maxAttempts));
                }
            }
        }

        // 配置路由和CORS处理
        server.createContext("/login", new CorsHandler(new LoginHandler()));
        server.createContext("/register", new CorsHandler(new RegisterHandler()));
        server.createContext("/", exchange -> {
            // 添加一个简单的健康检查端点
            String response = "Server is running";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        
        server.setExecutor(Executors.newFixedThreadPool(10));
        LOGGER.info("Server initialized on port " + port);
    }

    public void start() {
        server.start();
        LOGGER.info("Server is running on port " + port);
    }

    public void stop() {
        server.stop(0);
        DatabaseConnection.closeConnection();
        LOGGER.info("Server stopped");
    }

    public static void main(String[] args) {
        try {
            SimpleHttpServer httpServer = new SimpleHttpServer();
            httpServer.start();
            
            // 添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutting down server...");
                httpServer.stop();
            }));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start server", e);
        }
    }
}

class CorsHandler implements HttpHandler {
    private final HttpHandler handler;
    private static final Logger LOGGER = Logger.getLogger(CorsHandler.class.getName());

    public CorsHandler(HttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // 添加所有必要的CORS头
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600");
            
            // 处理预检请求
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            
            handler.handle(exchange);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling request", e);
            String errorMessage = "Internal Server Error";
            exchange.sendResponseHeaders(500, errorMessage.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorMessage.getBytes());
            }
        }
    }
}
