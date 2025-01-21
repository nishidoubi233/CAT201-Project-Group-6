package usmshop_server.dao;

import usmshop_server.model.User;

import java.sql.*;

/*
    This DAO class is dedicated to CRUD operations on the "user table" in the database
 */
public class User_DAO {

    // create a new user
    public boolean createUser(User user) {
        // first get the new user_id
        String getMaxIdSql = "SELECT COALESCE(MAX(user_id) + 1, 0) FROM USER_TABLE";
        String insertSql = "INSERT INTO USER_TABLE (user_id, user_name, user_email, user_password, user_registerdate) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = getConnection()) {
            // first get the new ID
            int newId;
            try (PreparedStatement pstmt = conn.prepareStatement(getMaxIdSql);
                 ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                newId = rs.getInt(1);
            }
            
            // then insert the new user
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, newId);
                pstmt.setString(2, user.getUserName());
                pstmt.setString(3, user.getUserEmail());
                pstmt.setString(4, user.getPassword());
                int rows = pstmt.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // find user by ID
    public User findById(int userId) {
        String sql = "SELECT * FROM USER_TABLE WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // convert database record to User object
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setUserEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("user_password"));
                    // registration date can also be retrieved
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // find user by email (for login/registration)
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

    // get the connection to the database
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // or other driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // remote SQL database connection information
        String url = "jdbc:mysql://47.79.98.152:3306/shopping_data?useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "write your password here";
        return DriverManager.getConnection(url, user, password);
    }
}
