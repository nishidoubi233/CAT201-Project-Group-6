package usmshop_server.service;

import usmshop_server.dao.User_DAO;
import usmshop_server.model.User;

/*
    该class主要负责用户信息的业务逻辑
 */
public class User_Service {

    private User_DAO userDAO = new User_DAO();

    /*
        根据用户ID获取用户信息
     */
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    // 更新密码放在未来版本会实装
}
