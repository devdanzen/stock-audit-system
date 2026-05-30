package dao;

import db.DBConnection;
import model.EndBalance;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EndBalanceDAO {

    public int commitSnapshot(List<EndBalance> rows) {
        String sql = "INSERT INTO end_balance(item_id, period_date, end_balance, unit_cost, extended_cost) "
                   + "VALUES (?,?,?,?,?) "
                   + "ON DUPLICATE KEY UPDATE end_balance=VALUES(end_balance), "
                   + "unit_cost=VALUES(unit_cost), extended_cost=VALUES(extended_cost)";
        Connection c = null;
        try {
            c = DBConnection.connect();
            c.setAutoCommit(false);
            int n;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (EndBalance eb : rows) {
                    ps.setInt(1, eb.getItemId());
                    ps.setDate(2, Date.valueOf(eb.getPeriodDate()));
                    ps.setBigDecimal(3, eb.getEndBalance());
                    ps.setBigDecimal(4, eb.getUnitCost());
                    ps.setBigDecimal(5, eb.getExtendedCost());
                    ps.addBatch();
                }
                int[] res = ps.executeBatch();
                n = res.length;
            }
            c.commit();
            return n;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Commit snapshot failed (rolled back): " + e.getMessage(), e);
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) {}
        }
    }
}
