package usmshop_server.dao;

import java.sql.*;
import usmshop_server.model.Cart;
import java.util.ArrayList;
import java.util.List;

/*
    This DAO class is dedicated to CRUD operations on the "shopping cart table" in the database
 */
public class Cart_DAO {

    /*
        add a new cart item
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
        remove a cart item
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

    
    //   clear the cart of a user
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
        get the item ID and quantity in the cart of a user

        @param userId the ID of the user
        @return the list of cart items with item ID and quantity
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


    // get the connection to the SQL database
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        // remote SQL database connection information
        String url = "jdbc:mysql://47.79.98.152:3306/shopping_data?useSSL=false&characterEncoding=UTF-8";
        String user = "root";
        String password = "write your password here";
        return DriverManager.getConnection(url, user, password);
    }

    // find the cart item
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

    // update the quantity of a cart item
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
