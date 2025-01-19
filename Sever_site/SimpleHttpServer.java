import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    private HttpServer server;
    private static final int START_PORT = 8080;
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
                    throw new IOException("No available ports found");
                }
            }
        }

        // 创建上下文
        server.createContext("/login", new LoginHandler());
        server.createContext("/register", new RegisterHandler());
        
        // 设置线程池
        server.setExecutor(Executors.newFixedThreadPool(10));
    }

    public void start() {
        server.start();
        System.out.println("Server is running on port " + port);
    }

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
