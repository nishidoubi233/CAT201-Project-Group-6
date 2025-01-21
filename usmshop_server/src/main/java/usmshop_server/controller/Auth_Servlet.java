package usmshop_server.controller;

import usmshop_server.service.Auth_Service;
import usmshop_server.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/*
    This Servlet is used to handle the login and register logic
 */

@WebServlet("/auth")
public class Auth_Servlet extends HttpServlet {

    // use the Auth_Service to handle the login and register logic
    private Auth_Service authService = new Auth_Service();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // set the response type to JSON
        resp.setContentType("application/json;charset=UTF-8");

        // distinguish login and register by an "action" parameter
        String action = req.getParameter("action");
        PrintWriter out = resp.getWriter();

        if ("login".equalsIgnoreCase(action)) {
            // get the user name and password from the front end
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // login verification
            User user = authService.login(email, password);
            if (user != null) {
                // login success, return user information
                out.println("{ \"status\": \"ok\", \"message\": \"login success\", \"userId\": " + user.getUserId() + " }");
            } else {
                // login failed
                out.println("{ \"status\": \"fail\", \"message\": \"Incorrect username or password.\" }");
            }

        } else if ("register".equalsIgnoreCase(action)) {
            // get the registration information
            String userName = req.getParameter("userName");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            // register
            boolean success = authService.register(userName, email, password);
            if (success) {
                out.println("{ \"status\": \"ok\", \"message\": \"register success\" }");
            } else {
                out.println("{ \"status\": \"fail\", \"message\": \"register failed or email already exists\" }");
            }
        } else {
            // unknown operation
            out.println("{ \"status\": \"error\", \"message\": \"unknown operation\" }");
        }
    }
}

