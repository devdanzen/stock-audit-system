package dao;

import db.DBConnection;
import model.Outlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OutletDAO {

    private static final String SELECT =
        "SELECT o.outlet_id, o.outlet_code, o.outlet_name, o.status, "
      + "(SELECT COUNT(*) FROM master_item mi WHERE mi.outlet_id = o.outlet_id) AS item_count "
      + "FROM outlet o ";

    public List<Outlet> findAll() {
        return query(SELECT + "ORDER BY o.outlet_code", null);
    }

    public List<Outlet> findActive() {
        return query(SELECT + "WHERE o.status = 'Active' ORDER BY o.outlet_code", null);
    }

    public List<Outlet> search(String keyword) {
        String like = "%" + keyword + "%";
        return query(SELECT + "WHERE o.outlet_code LIKE ? OR o.outlet_name LIKE ? ORDER BY o.outlet_code",
                new String[]{like, like});
    }

    public int insert(Outlet o) {
        String sql = "INSERT INTO outlet(outlet_code, outlet_name, status) VALUES (?,?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getOutletCode());
            ps.setString(2, o.getOutletName());
            ps.setString(3, o.getStatus() == null ? "Active" : o.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert outlet failed: " + e.getMessage(), e);
        }
    }

    public boolean update(Outlet o) {
        String sql = "UPDATE outlet SET outlet_code=?, outlet_name=?, status=? WHERE outlet_id=?";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getOutletCode());
            ps.setString(2, o.getOutletName());
            ps.setString(3, o.getStatus());
            ps.setInt(4, o.getOutletId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update outlet failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM outlet WHERE outlet_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete outlet failed: " + e.getMessage(), e);
        }
    }

    private List<Outlet> query(String sql, String[] params) {
        List<Outlet> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Outlet o = new Outlet();
                    o.setOutletId(rs.getInt("outlet_id"));
                    o.setOutletCode(rs.getString("outlet_code"));
                    o.setOutletName(rs.getString("outlet_name"));
                    o.setStatus(rs.getString("status"));
                    o.setItemCount(rs.getInt("item_count"));
                    list.add(o);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Query outlet failed: " + e.getMessage(), e);
        }
    }
}
