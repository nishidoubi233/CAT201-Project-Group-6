import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseConnection {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Properties properties = new Properties();
    private static Connection connection;
    
    static {
        try {
            // 设置数据库连接参数，添加重连和超时设置
            properties.setProperty("db.url", 
                "jdbc:mysql://localhost:3306/cat201?" +
                "useSSL=false&" +
                "serverTimezone=UTC&" +
                "allowPublicKeyRetrieval=true&" +
                "autoReconnect=true&" +
                "connectTimeout=30000");
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "Chan771008");
            
            // 加载 MySQL 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 初始化数据库表
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
            
            try (var stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                LOGGER.info("Database table initialized successfully");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        int retries = 3;
        SQLException lastException = null;
        
        while (retries > 0) {
            try {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(
                        properties.getProperty("db.url"),
                        properties.getProperty("db.username"),
                        properties.getProperty("db.password")
                    );
                }
                // 测试连接是否有效
                if (connection.isValid(5)) {
                    return connection;
                }
            } catch (SQLException e) {
                lastException = e;
                LOGGER.log(Level.WARNING, "Failed to get database connection, retrying... (" + retries + " attempts left)");
            }
            retries--;
            try {
                Thread.sleep(1000); // 等待1秒后重试
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        if (lastException != null) {
            throw lastException;
        }
        throw new SQLException("Failed to establish database connection after multiple attempts");
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
        }
    }
}