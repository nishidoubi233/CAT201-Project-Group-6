package usmshop_server.service;

import usmshop_server.dao.Cart_DAO;
import usmshop_server.model.Cart;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
    该class主要负责购物车相关的业务逻辑
 */
public class Cart_Service {

    private Cart_DAO cartDAO = new Cart_DAO();

    /*
        添加商品到购物车
     */
    public boolean addItemToCart(int userId, int itemId, int quantity) {
        // 先查找是否已存在该商品
        Cart existingItem = cartDAO.findCartItem(userId, itemId);
        
        if (existingItem != null) {
            // 如果已存在，更新数量
            int newQuantity = existingItem.getQuantity() + quantity;
            return cartDAO.updateCartItemQuantity(existingItem.getCartId(), newQuantity);
        } else {
            // 如果不存在 , 则添加新记录
            return cartDAO.addCartItem(userId, itemId, quantity);
        }
    }

    /*
        移除购物车中的某一条记录
     */
    public boolean removeItem(int cartItemId) {
        return cartDAO.removeCartItem(cartItemId);
    }

    /*
        清空某个用户的购物车
     */
    public void clearCart(int userId) {
        cartDAO.clearCartByUserId(userId);
    }

    /*
        获取用户的购物车列表
     */
    public List<Cart> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    /*
     更新购物车商品数量
     */
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        return cartDAO.updateCartItemQuantity(cartItemId, newQuantity);
    }

    public boolean updateItemQuantity(int userId, int itemId, int quantity) {
        // 使用DAO层的方法而不是直接访问数据库
        Cart existingItem = cartDAO.findCartItem(userId, itemId);
        if (existingItem != null) {
            return cartDAO.updateCartItemQuantity(existingItem.getCartId(), quantity);
        }
        return false;
    }
}
