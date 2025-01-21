package usmshop_server.model;

/*
    This class corresponds to the shopping cart table (CART_TABLE) in the database
 */
public class Cart {
    private int cartId;
    private int userId;
    private int itemId;
    private int quantity;
    
    // Getters & Setters
    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
