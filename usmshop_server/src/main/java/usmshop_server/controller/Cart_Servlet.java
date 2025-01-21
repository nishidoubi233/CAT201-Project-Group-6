package usmshop_server.controller;

import usmshop_server.service.Cart_Service;
import usmshop_server.model.Cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

/*
    This Servlet is used to handle the CRUD operations on the shopping cart
 */

@WebServlet("/cart")
public class Cart_Servlet extends HttpServlet {

    private Cart_Service cartService = new Cart_Service();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // set the response type to JSON
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action"); 
        // possible actions: "add", "clear", "update"

        if ("add".equalsIgnoreCase(action)) {
            // add to cart
            String userIdParam = req.getParameter("userId");
            String itemIdParam = req.getParameter("itemId");
            String quantityParam = req.getParameter("quantity");
    
            try {
                int userId = Integer.parseInt(userIdParam);
                int itemId = Integer.parseInt(itemIdParam);
                int quantity = Integer.parseInt(quantityParam);

                boolean success = cartService.addItemToCart(userId, itemId, quantity);
                if (success) {
                    out.println("{ \"status\": \"ok\", \"message\": \"add success\" }");
                } else {
                    out.println("{ \"status\": \"fail\", \"message\": \"add failed or item already exists\" }");
                }
            } catch (NumberFormatException e) {
                out.println("{ \"status\": \"fail\", \"message\": \"parameter format error\" }");
            }

        } else if ("clear".equalsIgnoreCase(action)) {
            // clear the cart
            String userIdParam = req.getParameter("userId");
            try {
                int userId = Integer.parseInt(userIdParam);
                cartService.clearCart(userId);
                out.println("{ \"status\": \"ok\", \"message\": \"cart cleared\" }");
            } catch (NumberFormatException e) {
                out.println("{ \"status\": \"fail\", \"message\": \"user id format error\" }");
            }
        } else if ("update".equalsIgnoreCase(action)) {
            String userIdParam = req.getParameter("userId");
            String itemIdParam = req.getParameter("itemId");
            String quantityParam = req.getParameter("quantity");

            try {
                int userId = Integer.parseInt(userIdParam);
                int itemId = Integer.parseInt(itemIdParam);
                int quantity = Integer.parseInt(quantityParam);

                boolean success = cartService.updateItemQuantity(userId, itemId, quantity);
                if (success) {
                    out.println("{ \"status\": \"ok\", \"message\": \"quantity updated\" }");
                } else {
                    out.println("{ \"status\": \"fail\", \"message\": \"update failed\" }");
                }
            } catch (NumberFormatException e) {
                out.println("{ \"status\": \"fail\", \"message\": \"parameter format error\" }");
            }
        } else {
            out.println("{ \"status\": \"error\", \"message\": \"unknown operation\" }");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // delete an item from the cart
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // simplified assumption: delete by cartItemId
        String cartItemIdParam = req.getParameter("cartItemId");
        try {
            int cartItemId = Integer.parseInt(cartItemIdParam);
            boolean success = cartService.removeItem(cartItemId);
            if (success) {
                out.println("{ \"status\": \"ok\", \"message\": \"item deleted\" }");
            } else {
                out.println("{ \"status\": \"fail\", \"message\": \"delete failed or item not found\" }");
            }
        } catch (NumberFormatException e) {
            out.println("{ \"status\": \"fail\", \"message\": \"cartItemId format error\" }");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        // add CORS headers
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE");
        
        PrintWriter out = resp.getWriter();

        String userIdParam = req.getParameter("userId");
        try {
            int userId = Integer.parseInt(userIdParam);
            List<Cart> items = cartService.getCartItems(userId);
            Gson gson = new Gson();
            out.println("{ \"status\": \"ok\", \"items\": " + gson.toJson(items) + " }");
        } catch (NumberFormatException e) {
            out.println("{ \"status\": \"fail\", \"message\": \"Invalid user ID\" }");
        }
    }
}
