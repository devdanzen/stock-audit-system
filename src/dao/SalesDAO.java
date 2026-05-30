package dao;

import db.DBConnection;
import model.SalesDetail;
import model.SalesHeader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class SalesDAO {

    public int insertWithDetails(SalesHeader h, List<SalesDetail> lines) {
        String hSql = "INSERT INTO sales_header(invoice_number, sale_date, outlet_id, total_amount) VALUES (?,?,?,?)";
        String dSql = "INSERT INTO sales_detail(sales_id, item_id, quantity, unit_price, extended_price) VALUES (?,?,?,?,?)";
        Connection c = null;
        try {
            c = DBConnection.connect();
            c.setAutoCommit(false);

            int salesId;
            try (PreparedStatement ps = c.prepareStatement(hSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, h.getInvoiceNumber());
                ps.setDate(2, Date.valueOf(h.getSaleDate()));
                if (h.getOutletId() == null) ps.setNull(3, java.sql.Types.INTEGER); else ps.setInt(3, h.getOutletId());
                ps.setBigDecimal(4, h.getTotalAmount());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    salesId = keys.next() ? keys.getInt(1) : 0;
                }
            }

            try (PreparedStatement ps = c.prepareStatement(dSql)) {
                for (SalesDetail d : lines) {
                    ps.setInt(1, salesId);
                    ps.setInt(2, d.getItemId());
                    ps.setBigDecimal(3, d.getQuantity());
                    ps.setBigDecimal(4, d.getUnitPrice());
                    ps.setBigDecimal(5, d.getExtendedPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            c.commit();
            return salesId;
        } catch (SQLException e) {
            rollback(c);
            throw new RuntimeException("Save sale failed (rolled back): " + e.getMessage(), e);
        } finally {
            closeAuto(c);
        }
    }

    public String getNextInvoiceNumber() {
        int year = LocalDate.now().getYear();
        String prefix = "INV-" + year + "-";
        String sql = "SELECT invoice_number FROM sales_header WHERE invoice_number LIKE ? "
                   + "ORDER BY invoice_number DESC LIMIT 1";
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
            throw new RuntimeException("Generate invoice number failed: " + e.getMessage(), e);
        }
    }

    public static BigDecimal lineTotal(BigDecimal qty, BigDecimal price) {
        if (qty == null || price == null) return BigDecimal.ZERO;
        return qty.multiply(price);
    }

    private void rollback(Connection c) {
        if (c != null) try { c.rollback(); } catch (SQLException ignored) {}
    }

    private void closeAuto(Connection c) {
        if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) {}
    }
}
