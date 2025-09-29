package model;

public class LoggedInUser {
    private int userId;
    private String role;
    private String fullName;

    public LoggedInUser(int userId, String role, String fullName) {
        this.userId = userId;
        this.role = role;
        this.fullName = fullName;
    }

    public int getUserId() { return userId; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
}
