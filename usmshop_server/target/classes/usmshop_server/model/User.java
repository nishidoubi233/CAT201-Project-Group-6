package usmshop_server.model;

/*
    This class corresponds to the user table (USER_TABLE) in the database
*/
public class User {
    private int userId;
    private String userName;
    private String userEmail;
    private String password;  
    
    // Constructor
    public User() {
    }

    // Getters & Setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
