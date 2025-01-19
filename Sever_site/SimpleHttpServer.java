import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
        
        // 注册处理器
        server.createContext("/login", new LoginHandler());
        server.createContext("/register", new RegisterHandler());
        
        // 使用线程池
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        
        System.out.println("Server is running on port 3000");
    }
}
