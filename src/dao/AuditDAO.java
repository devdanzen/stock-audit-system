package dao;

import db.DBConnection;
import model.Audit;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AuditDAO {

    /**
     * Saves all counted rows in one transaction. UNIQUE(item_id, audit_date) means a
     * re-count on the same date overwrites the previous one (ON DUPLICATE KEY UPDATE).
     */
    public int saveAll(List<Audit> rows) {
        String sql = "INSERT INTO audit(item_id, category_id, audit_date, audit_quantity, variance, note) "
                   + "VALUES (?,?,?,?,?,?) "
                   + "ON DUPLICATE KEY UPDATE audit_quantity=VALUES(audit_quantity), "
                   + "variance=VALUES(variance), note=VALUES(note), category_id=VALUES(category_id)";
        Connection c = null;
        try {
            c = DBConnection.connect();
            c.setAutoCommit(false);
            int n;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (Audit a : rows) {
                    ps.setInt(1, a.getItemId());
                    if (a.getCategoryId() == null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, a.getCategoryId());
                    ps.setDate(3, Date.valueOf(a.getAuditDate()));
                    ps.setBigDecimal(4, a.getAuditQuantity());
                    ps.setBigDecimal(5, a.getVariance());
                    ps.setString(6, a.getNote());
                    ps.addBatch();
                }
                int[] res = ps.executeBatch();
                n = res.length;
            }
            c.commit();
            return n;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Save audit failed (rolled back): " + e.getMessage(), e);
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) {}
        }
    }
}
