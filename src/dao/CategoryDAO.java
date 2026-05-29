package dao;

import db.DBConnection;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final String SELECT =
        "SELECT c.category_id, c.category_code, c.category_name, "
      + "(SELECT COUNT(*) FROM master_item mi WHERE mi.category_id = c.category_id) AS item_count "
      + "FROM category c ";

    public List<Category> findAll() {
        return query(SELECT + "ORDER BY c.category_code", null);
    }

    public List<Category> search(String keyword) {
        String like = "%" + keyword + "%";
        return query(SELECT + "WHERE c.category_code LIKE ? OR c.category_name LIKE ? ORDER BY c.category_code",
                new String[]{like, like});
    }

    public Category findById(int id) {
        List<Category> l = query(SELECT + "WHERE c.category_id = ?", new String[]{String.valueOf(id)});
        return l.isEmpty() ? null : l.get(0);
    }

    public int insert(Category cat) {
        String sql = "INSERT INTO category(category_code, category_name) VALUES (?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getCategoryCode().toUpperCase());
            ps.setString(2, cat.getCategoryName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert category failed: " + e.getMessage(), e);
        }
    }

    public boolean update(Category cat) {
        String sql = "UPDATE category SET category_code=?, category_name=? WHERE category_id=?";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getCategoryCode().toUpperCase());
            ps.setString(2, cat.getCategoryName());
            ps.setInt(3, cat.getCategoryId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update category failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM category WHERE category_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete category failed: " + e.getMessage(), e);
        }
    }

    private List<Category> query(String sql, String[] params) {
        List<Category> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category cat = new Category();
                    cat.setCategoryId(rs.getInt("category_id"));
                    cat.setCategoryCode(rs.getString("category_code"));
                    cat.setCategoryName(rs.getString("category_name"));
                    cat.setItemCount(rs.getInt("item_count"));
                    list.add(cat);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Query category failed: " + e.getMessage(), e);
        }
    }
}
