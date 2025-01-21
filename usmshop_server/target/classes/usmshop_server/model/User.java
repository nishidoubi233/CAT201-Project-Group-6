package usmshop_server.model;

/*
    此class对应数据库中的用户表（USER_TABLE）
*/
public class User {
    private int userId;
    private String userName;
    private String userEmail;
    private String password;  
    
    // 构造函数
    public User() {
    }

    // getter & setter
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
