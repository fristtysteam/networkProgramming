package business;

import java.util.Objects;
public class User {
    private String username;
    private String password;
    private boolean AdminStatus;

    public User() {

    }

    public User(String username, String password, boolean adminStatus) {
        this.password = password;
        this.username = username;
        this.AdminStatus = adminStatus;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdminStatus() {
        return AdminStatus;
    }

    public void setAdminStatus(boolean adminStatus) {
        AdminStatus = adminStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}