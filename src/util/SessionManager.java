package util;

import model.User;

public final class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    private SessionManager() {}

    public static SessionManager get() { return INSTANCE; }

    public void login(User user) { this.currentUser = user; }
    public void logout() { this.currentUser = null; }

    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }

    public String getRole() {
        return currentUser == null ? null : currentUser.getRole();
    }

    public Integer getOutletId() {
        return currentUser == null ? null : currentUser.getOutletId();
    }

    public boolean isAdministrator() { return "Administrator".equals(getRole()); }
    public boolean isManager()       { return "Manager".equals(getRole()); }
    public boolean isStaff()         { return "Staff".equals(getRole()); }
}
