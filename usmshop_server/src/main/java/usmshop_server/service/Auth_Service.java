package usmshop_server.service;

import usmshop_server.dao.User_DAO;
import usmshop_server.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/*
    该class主要负责用户登录和注册的逻辑
 */
public class Auth_Service {

    private User_DAO userDAO = new User_DAO();

    /*
        登录业务逻辑：根据email查询用户，并比对密码哈希
     */
    public User login(String email, String password) {
        // 根据邮箱查找用户
        User user = userDAO.findByEmail(email);
        if (user == null) {
            return null; // 用户不存在
        }

        // 对输入的密码进行哈希，然后与存储的哈希值比对
        String hashedPassword = hashPassword(password);
        if (hashedPassword != null && hashedPassword.equals(user.getPassword())) {
            return user; // 登录成功
        }
        return null; // 密码错误
    }

    /*
     注册账户主逻辑: 检查邮箱是否已经存在，否则创建新用户
     */
    public boolean register(String userName, String email, String password) {
        // 先检查邮箱是否占用
        User existing = userDAO.findByEmail(email);
        if (existing != null) {
            return false; // 已存在同邮箱用户
        }

        // 创建新用户，存储密码哈希值
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return false; // 哈希处理失败
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(email);
        newUser.setPassword(hashedPassword);

        return userDAO.createUser(newUser);
    }

    /*
     使用SHA-256进行密码哈希
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
