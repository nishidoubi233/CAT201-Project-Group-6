package usmshop_server.controller;

import usmshop_server.service.Auth_Service;
import usmshop_server.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/*
    该Servlet用于处理用户的登录和注册
 */

@WebServlet("/api/auth")
public class Auth_Servlet extends HttpServlet {

    //用于处理登录和注册的逻辑
    private Auth_Service authService = new Auth_Service();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 添加CORS响应头
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        
        // 设置返回类型为JSON
        resp.setContentType("application/json;charset=UTF-8");

        // 通过一个 "action" 参数来区分是 login 还是 register
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        if ("login".equalsIgnoreCase(action)) {
            // 获取前端传来的用户名和密码
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // 进行登录验证
            User user = authService.login(email, password);
            if (user != null) {
                // 登录成功，返回用户信息
                out.println("{ \"status\": \"ok\", \"message\": \"login success\", \"userId\": " + user.getUserId() + " }");
            } else {
                // 登录失败
                out.println("{ \"status\": \"fail\", \"message\": \"Incorrect username or password.\" }");
            }

        } else if ("register".equalsIgnoreCase(action)) {
            // 获取注册信息
            String userName = req.getParameter("userName");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // 进行注册
            boolean success = authService.register(userName, email, password);
            if (success) {
                out.println("{ \"status\": \"ok\", \"message\": \"register success\" }");
            } else {
                out.println("{ \"status\": \"fail\", \"message\": \"register failed or email already exists\" }");
            }
        } else {
            // 未知操作
            out.println("{ \"status\": \"error\", \"message\": \"unknown operation\" }");
        }
    }

    // 添加OPTIONS请求处理
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}

