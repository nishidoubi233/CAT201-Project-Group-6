import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * 简单的 HTTP 服务器实现
 * 处理登录和注册请求
 */
public class SimpleHttpServer {
    private HttpServer server;
    private static final int START_PORT = 8080;  // 起始端口号
    private int port;

    public SimpleHttpServer() throws IOException {
        // 从8080开始尝试，直到找到可用端口
        port = START_PORT;
        while (true) {
            try {
                server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
                break;
            } catch (IOException e) {
                port++;
                if (port > START_PORT + 100) { // 最多尝试100个端口
                    throw new IOException("没有找到可用的端口");
                }
            }
        }

        // 注册 HTTP 路由处理器
        server.createContext("/login", new LoginHandler());
        server.createContext("/register", new RegisterHandler());
        
        // 创建固定大小为10的线程池
        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    /**
     * 启动服务器
     */
    public void start() {
        server.start();
        System.out.println("Server is running on port " + port);
    }

    /**
     * 停止服务器
     */
    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) {
        try {
            SimpleHttpServer httpServer = new SimpleHttpServer();
            httpServer.start();    
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
