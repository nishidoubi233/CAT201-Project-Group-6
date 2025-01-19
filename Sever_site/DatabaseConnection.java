import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接管理类
 * 实现单例模式，管理与MySQL数据库的连接
 */
public class DatabaseConnection {
    private static Properties properties;
    private static Connection connection;
    
    // 静态初始化块，加载数据库配置和驱动
    static {
        properties = new Properties();
        // 设置数据库连接参数
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/cat201");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "Chan771008");
        
        // 加载 MySQL 驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到 MySQL JDBC 驱动", e);
        }
    }

    /**
     * 获取数据库连接
     * 如果连接不存在或已关闭，则创建新连接
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("关闭数据库连接时出错", e);
            }
        }
    }
}
