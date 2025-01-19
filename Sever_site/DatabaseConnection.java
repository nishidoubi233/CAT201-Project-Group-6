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
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/cat201");
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "");
            
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.info("Database driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database driver not found. Please add mysql-connector-java to your classpath", e);
            throw new RuntimeException("Database initialization failed", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database initialization error", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(1)) {
            connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
            LOGGER.info("Database connection established");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                LOGGER.info("Database connection closed successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }
}