package usmshop_server.service;

import usmshop_server.dao.User_DAO;
import usmshop_server.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/*
    this class is used to handle the login and register logic
 */
public class Auth_Service {

    private User_DAO userDAO = new User_DAO();

    /*
        login logic: find user by email and compare the hashed password
     */
    public User login(String email, String password) {
        // find user by email
        User user = userDAO.findByEmail(email);
        if (user == null) {
            return null; // user not found
        }

        // hash the input password and compare it with the stored hashed password
        String hashedPassword = hashPassword(password);
        if (hashedPassword != null && hashedPassword.equals(user.getPassword())) {
            return user; // login success
        }
        return null; // password error
    }

    /*
        register logic: check if the email is already taken, otherwise create a new user
     */
    public boolean register(String userName, String email, String password) {
        // check if the email is already taken
        User existing = userDAO.findByEmail(email);
        if (existing != null) {
            return false; // email already taken
        }

        // create a new user and store the hashed password
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return false; // hashing failed
        }

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserEmail(email);
        newUser.setPassword(hashedPassword);

        return userDAO.createUser(newUser);
    }

    /*
        use SHA-256 to hash the password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // convert the byte array to a hexadecimal string
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
