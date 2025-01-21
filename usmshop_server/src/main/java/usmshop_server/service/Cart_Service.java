package usmshop_server.service;

import usmshop_server.dao.Cart_DAO;
import usmshop_server.model.Cart;
import java.util.List;

/*
    This class is responsible for shopping cart related business logic
 */
public class Cart_Service {

    private Cart_DAO cartDAO = new Cart_DAO();

    /*
        Add item to shopping cart
     */
    public boolean addItemToCart(int userId, int itemId, int quantity) {
        // First check if the item already exists
        Cart existingItem = cartDAO.findCartItem(userId, itemId);
        
        if (existingItem != null) {
            // If exists, update the quantity
            int newQuantity = existingItem.getQuantity() + quantity;
            return cartDAO.updateCartItemQuantity(existingItem.getCartId(), newQuantity);
        } else {
            // If not exists, add new record
            return cartDAO.addCartItem(userId, itemId, quantity);
        }
    }

    /*
        Remove an item from shopping cart
     */
    public boolean removeItem(int cartItemId) {
        return cartDAO.removeCartItem(cartItemId);
    }

    /*
        Clear user's shopping cart
     */
    public void clearCart(int userId) {
        cartDAO.clearCartByUserId(userId);
    }

    /*
        Get user's shopping cart items
     */
    public List<Cart> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    /*
        Update cart item quantity
     */
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        return cartDAO.updateCartItemQuantity(cartItemId, newQuantity);
    }

    public boolean updateItemQuantity(int userId, int itemId, int quantity) {
        // Use DAO method instead of accessing database directly
        Cart existingItem = cartDAO.findCartItem(userId, itemId);
        if (existingItem != null) {
            return cartDAO.updateCartItemQuantity(existingItem.getCartId(), quantity);
        }
        return false;
    }
}
