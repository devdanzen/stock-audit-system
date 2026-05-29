package dao;

import db.DBConnection;
import model.Vendor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendorDAO {

    private static final String SELECT =
        "SELECT v.vendor_id, v.vendor_code, v.vendor_name, v.contact_phone, v.status, "
      + "(SELECT COUNT(*) FROM receiving_header rh WHERE rh.vendor_id = v.vendor_id) AS total_receipts "
      + "FROM vendor v ";

    public List<Vendor> findAll() {
        return query(SELECT + "ORDER BY v.vendor_code", null);
    }

    public List<Vendor> findActive() {
        return query(SELECT + "WHERE v.status = 'Active' ORDER BY v.vendor_code", null);
    }

    public List<Vendor> search(String keyword) {
        String like = "%" + keyword + "%";
        return query(SELECT + "WHERE v.vendor_code LIKE ? OR v.vendor_name LIKE ? ORDER BY v.vendor_code",
                new String[]{like, like});
    }

    public int insert(Vendor v) {
        String sql = "INSERT INTO vendor(vendor_code, vendor_name, contact_phone, status) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getVendorCode());
            ps.setString(2, v.getVendorName());
            ps.setString(3, v.getContactPhone());
            ps.setString(4, v.getStatus() == null ? "Active" : v.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert vendor failed: " + e.getMessage(), e);
        }
    }

    public boolean update(Vendor v) {
        String sql = "UPDATE vendor SET vendor_code=?, vendor_name=?, contact_phone=?, status=? WHERE vendor_id=?";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getVendorCode());
            ps.setString(2, v.getVendorName());
            ps.setString(3, v.getContactPhone());
            ps.setString(4, v.getStatus());
            ps.setInt(5, v.getVendorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update vendor failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM vendor WHERE vendor_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete vendor failed: " + e.getMessage(), e);
        }
    }

    private List<Vendor> query(String sql, String[] params) {
        List<Vendor> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vendor v = new Vendor();
                    v.setVendorId(rs.getInt("vendor_id"));
                    v.setVendorCode(rs.getString("vendor_code"));
                    v.setVendorName(rs.getString("vendor_name"));
                    v.setContactPhone(rs.getString("contact_phone"));
                    v.setStatus(rs.getString("status"));
                    v.setTotalReceipts(rs.getInt("total_receipts"));
                    list.add(v);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Query vendor failed: " + e.getMessage(), e);
        }
    }
}
