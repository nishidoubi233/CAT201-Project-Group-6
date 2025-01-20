package usmshop_server.dao;

import java.sql.*;
import usmshop_server.model.Cart;
import java.util.ArrayList;
import java.util.List;

/*
    该DAO类专门与数据库中的"购物车表"进行CRUD操作
 */
public class Cart_DAO {

    /*
       添加一条购物车记录
     */
    public boolean addCartItem(int userId, int itemId, int quantity) {
        String sql = "INSERT INTO CART_TABLE (user_id, item_id, quantity, added_date) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantity);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
        移除一条购物车记录
     */
    public boolean removeCartItem(int cartItemId) {
        String sql = "DELETE FROM CART_TABLE WHERE cart_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartItemId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    //   清空某个用户的购物车
    public void clearCartByUserId(int userId) {
        String sql = "DELETE FROM CART_TABLE WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        获取用户购物车中的物品ID和数量

        @param userId 用户ID
        @return 包含物品ID和数量的购物车列表
     */
    public List<Cart> getCartItems(int userId) {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT item_id, quantity FROM CART_TABLE WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cart cart = new Cart();
                    cart.setItemId(rs.getInt("item_id"));
                    cart.setQuantity(rs.getInt("quantity"));
                    cartItems.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cartItems;
    }


    // 获取SQL数据库连接
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // SQL数据库连接信息
        String url = "jdbc:mysql://localhost:3306/usmshop?useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "123456";
        return DriverManager.getConnection(url, user, password);
    }

    // 查找购物车中的记录
    public Cart findCartItem(int userId, int itemId) {
        String sql = "SELECT * FROM CART_TABLE WHERE user_id = ? AND item_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("cart_id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setItemId(rs.getInt("item_id"));
                    cart.setQuantity(rs.getInt("quantity"));
                    return cart;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新购物车中某一条记录的数量
    public boolean updateCartItemQuantity(int cartId, int quantity) {
        String sql = "UPDATE CART_TABLE SET quantity = ? WHERE cart_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, cartId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
