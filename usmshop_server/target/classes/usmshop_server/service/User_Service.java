package usmshop_server.service;

import usmshop_server.dao.User_DAO;
import usmshop_server.model.User;

/*
    This class is responsible for user information business logic
 */
public class User_Service {

    private User_DAO userDAO = new User_DAO();

    /*
        Get user information by user ID
     */
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    // Password update will be implemented in future versions
}
