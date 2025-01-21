package usmshop_server.controller;

import usmshop_server.service.User_Service;
import usmshop_server.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/*
    该Servlet用于处理用户的查询、更新等操作
 */
@WebServlet("/user")
public class User_Servlet extends HttpServlet {

    private User_Service userService = new User_Service();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置返回类型为JSON

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String userIdParam = req.getParameter("id"); 
        if (userIdParam == null) {
            out.println("{ \"status\": \"fail\", \"message\": \"no user id\" }");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            out.println("{ \"status\": \"fail\", \"message\": \"user id format error\" }");
            return;
        }

        // 获取用户信息
        User user = userService.getUserById(userId);
        if (user != null) {

            // 返回用户信息
            out.println("{");
            out.println("  \"status\": \"ok\",");
            out.println("  \"user\": {");
            out.println("     \"userId\": " + user.getUserId() + ",");
            out.println("     \"userName\": \"" + user.getUserName() + "\",");
            out.println("     \"email\": \"" + user.getUserEmail() + "\"");
            out.println("  }");
            out.println("}");
        } else {
            out.println("{ \"status\": \"fail\", \"message\": \"user not found\" }");
        }
    }
}
