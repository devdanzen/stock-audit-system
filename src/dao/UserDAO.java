package dao;

import db.DBConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Returns the authenticated active user, or null if username/password invalid. */
    public User login(String username, String password) {
        String sql = "SELECT * FROM `user` WHERE username = ? AND status = 'Active'";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    if (hash != null && BCrypt.checkpw(password, hash)) {
                        User u = map(rs);
                        updateLastLogin(u.getUserId());
                        return u;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Login query failed: " + e.getMessage(), e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT u.*, o.outlet_name FROM `user` u "
                   + "LEFT JOIN outlet o ON o.outlet_id = u.outlet_id ORDER BY u.username";
        List<User> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = map(rs);
                u.setOutletName(rs.getString("outlet_name"));
                list.add(u);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findAll users failed: " + e.getMessage(), e);
        }
    }

    public int insert(User u, String plainPassword) {
        String sql = "INSERT INTO `user`(username, password_hash, role, outlet_id, status) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
            ps.setString(3, u.getRole());
            setIntOrNull(ps, 4, u.getOutletId());
            ps.setString(5, u.getStatus() == null ? "Active" : u.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Insert user failed: " + e.getMessage(), e);
        }
    }

    /** If plainPassword is null/blank, keeps the existing password. */
    public boolean update(User u, String plainPassword) {
        boolean changePw = plainPassword != null && !plainPassword.trim().isEmpty();
        String sql = "UPDATE `user` SET username=?, role=?, outlet_id=?, status=?"
                   + (changePw ? ", password_hash=?" : "") + " WHERE user_id=?";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, u.getUsername());
            ps.setString(i++, u.getRole());
            setIntOrNull(ps, i++, u.getOutletId());
            ps.setString(i++, u.getStatus());
            if (changePw) ps.setString(i++, BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
            ps.setInt(i, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update user failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(int userId) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM `user` WHERE user_id=?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete user failed: " + e.getMessage(), e);
        }
    }

    private void updateLastLogin(int userId) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("UPDATE `user` SET last_login_at = NOW() WHERE user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            // last-login is non-critical
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        int outlet = rs.getInt("outlet_id");
        u.setOutletId(rs.wasNull() ? null : outlet);
        u.setStatus(rs.getString("status"));
        return u;
    }

    private void setIntOrNull(PreparedStatement ps, int idx, Integer v) throws SQLException {
        if (v == null) ps.setNull(idx, java.sql.Types.INTEGER);
        else ps.setInt(idx, v);
    }
}
