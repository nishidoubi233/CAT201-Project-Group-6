package usmshop_server.dao;

import usmshop_server.model.User;

import java.sql.*;

/*
    该DAO类专门与数据库中的"用户表"进行CRUD操作
 */
public class User_DAO {

    // 创建新用户
    public boolean createUser(User user) {
        String sql = "INSERT INTO USER_TABLE (user_name, user_email, user_password, user_registerdate) VALUES (?, ?, ?, NOW())";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserEmail());
            pstmt.setString(3, user.getPassword());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根据ID查找用户
    public User findById(int userId) {
        String sql = "SELECT * FROM USER_TABLE WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 把数据库记录转换为User对象
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("user_password"));
                    // 注册日期也可以取出
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据Email查找用户（用于登录/注册判断）
    public User findByEmail(String email) {
        String sql = "SELECT * FROM USER_TABLE WHERE user_email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("user_password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取数据库连接信息
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // or other driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 远程SQL数据库连接信息
        String url = "jdbc:mysql://47.79.98.152:3306/shopping_data?useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "Chan771008";
        return DriverManager.getConnection(url, user, password);
    }
}
