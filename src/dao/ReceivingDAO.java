package dao;

import db.DBConnection;
import model.ReceivingDetail;
import model.ReceivingHeader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class ReceivingDAO {

    /** Atomic save: one receiving_header + N receiving_detail. Saved as 'Posted' so SOH counts it. */
    public int insertWithDetails(ReceivingHeader h, List<ReceivingDetail> lines) {
        String hSql = "INSERT INTO receiving_header(receipt_number, receipt_type, receipt_date, po_number, "
                    + "vendor_id, outlet_id, posting_status, posted_user_id) VALUES (?,?,?,?,?,?,?,?)";
        String dSql = "INSERT INTO receiving_detail(receiving_id, item_id, qty_received, qty_invoiced, "
                    + "qty_returned, unit, unit_cost, extended_cost) VALUES (?,?,?,?,?,?,?,?)";
        Connection c = null;
        try {
            c = DBConnection.connect();
            c.setAutoCommit(false);

            int receivingId;
            try (PreparedStatement ps = c.prepareStatement(hSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, h.getReceiptNumber());
                ps.setString(2, h.getReceiptType());
                ps.setDate(3, Date.valueOf(h.getReceiptDate()));
                ps.setString(4, h.getPoNumber());
                if (h.getVendorId() == null) ps.setNull(5, java.sql.Types.INTEGER); else ps.setInt(5, h.getVendorId());
                if (h.getOutletId() == null) ps.setNull(6, java.sql.Types.INTEGER); else ps.setInt(6, h.getOutletId());
                ps.setString(7, h.getPostingStatus() == null ? "Posted" : h.getPostingStatus());
                if (h.getPostedUserId() == null) ps.setNull(8, java.sql.Types.INTEGER); else ps.setInt(8, h.getPostedUserId());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    receivingId = keys.next() ? keys.getInt(1) : 0;
                }
            }

            try (PreparedStatement ps = c.prepareStatement(dSql)) {
                for (ReceivingDetail d : lines) {
                    ps.setInt(1, receivingId);
                    ps.setInt(2, d.getItemId());
                    ps.setBigDecimal(3, d.getQtyReceived());
                    ps.setBigDecimal(4, d.getQtyInvoiced() == null ? d.getQtyReceived() : d.getQtyInvoiced());
                    ps.setBigDecimal(5, d.getQtyReturned() == null ? java.math.BigDecimal.ZERO : d.getQtyReturned());
                    ps.setString(6, d.getUnit());
                    ps.setBigDecimal(7, d.getUnitCost());
                    ps.setBigDecimal(8, d.getExtendedCost());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            c.commit();
            return receivingId;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Save receipt failed (rolled back): " + e.getMessage(), e);
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) {}
        }
    }

    public String getNextReceiptNumber() {
        int year = LocalDate.now().getYear();
        String prefix = "RCV-" + year + "-";
        String sql = "SELECT receipt_number FROM receiving_header WHERE receipt_number LIKE ? "
                   + "ORDER BY receipt_number DESC LIMIT 1";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                int next = 1;
                if (rs.next()) {
                    String last = rs.getString(1);
                    try { next = Integer.parseInt(last.substring(prefix.length())) + 1; }
                    catch (NumberFormatException ignored) {}
                }
                return prefix + String.format("%05d", next);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Generate receipt number failed: " + e.getMessage(), e);
        }
    }
}
