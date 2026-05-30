package dao;

import db.DBConnection;
import model.NameValue;
import model.StockOnHandRow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public List<StockOnHandRow> stockOnHand(Integer categoryId, Integer outletId,
                                            boolean activeOnly, String search) {
        StringBuilder sql = new StringBuilder(
            "SELECT v.item_id, v.item_code, v.description, v.soh_computed, "
          + "mi.current_cost, mi.base_unit, c.category_name, o.outlet_name "
          + "FROM v_stock_on_hand v "
          + "JOIN master_item mi ON mi.item_id = v.item_id "
          + "LEFT JOIN category c ON c.category_id = mi.category_id "
          + "LEFT JOIN outlet o ON o.outlet_id = mi.outlet_id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (categoryId != null) { sql.append("AND mi.category_id = ? "); params.add(categoryId); }
        if (outletId != null)   { sql.append("AND mi.outlet_id = ? ");   params.add(outletId); }
        if (activeOnly)         { sql.append("AND mi.status = 'Active' "); }
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (v.item_code LIKE ? OR v.description LIKE ?) ");
            params.add("%" + search + "%"); params.add("%" + search + "%");
        }
        sql.append("ORDER BY v.item_code");

        List<StockOnHandRow> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockOnHandRow r = new StockOnHandRow();
                    r.setItemId(rs.getInt("item_id"));
                    r.setItemCode(rs.getString("item_code"));
                    r.setDescription(rs.getString("description"));
                    r.setCategoryName(rs.getString("category_name"));
                    r.setOutletName(rs.getString("outlet_name"));
                    r.setBaseUnit(rs.getString("base_unit"));
                    BigDecimal soh = rs.getBigDecimal("soh_computed");
                    BigDecimal cost = rs.getBigDecimal("current_cost");
                    if (soh == null) soh = BigDecimal.ZERO;
                    if (cost == null) cost = BigDecimal.ZERO;
                    r.setOnHand(soh);
                    r.setUnitCost(cost);
                    r.setStockValue(soh.multiply(cost));
                    list.add(r);
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("stockOnHand query failed: " + e.getMessage(), e);
        }
    }

    public List<NameValue> topSellers(int limit) {
        return nameValue("SELECT description, total_qty_sold FROM v_top_sellers LIMIT " + limit);
    }

    public List<NameValue> topWaste(int limit) {
        return nameValue("SELECT description, total_waste_cost FROM v_top_waste LIMIT " + limit);
    }

    public List<NameValue> dailySales(int days) {
        return nameValue("SELECT DATE_FORMAT(sale_date,'%d %b') AS lbl, daily_revenue FROM v_daily_sales "
                       + "WHERE sale_date >= (CURDATE() - INTERVAL " + days + " DAY) ORDER BY sale_date");
    }

    public List<NameValue> salesByCategory() {
        return nameValue("SELECT c.category_name, SUM(sd.extended_price) "
                       + "FROM sales_detail sd JOIN master_item mi ON mi.item_id = sd.item_id "
                       + "JOIN category c ON c.category_id = mi.category_id "
                       + "GROUP BY c.category_name ORDER BY 2 DESC");
    }

    private List<NameValue> nameValue(String sql) {
        List<NameValue> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String label = rs.getString(1);
                double value = rs.getDouble(2);
                list.add(new NameValue(label == null ? "?" : label, value));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Chart query failed: " + e.getMessage(), e);
        }
    }

    public BigDecimal todaySales() {
        String sql = "SELECT COALESCE(SUM(sd.extended_price),0) FROM sales_detail sd "
                   + "JOIN sales_header sh ON sh.sales_id = sd.sales_id WHERE sh.sale_date = CURDATE()";
        return scalarMoney(sql);
    }

    public int lowStockCount() {
        return scalarInt("SELECT COUNT(*) FROM v_stock_on_hand WHERE soh_computed < 30");
    }

    public int pendingAudits() {
        return scalarInt("SELECT COUNT(*) FROM master_item WHERE status='Active' "
                       + "AND item_id NOT IN (SELECT item_id FROM audit)");
    }

    public int totalItems() {
        return scalarInt("SELECT COUNT(*) FROM master_item");
    }

    public List<Object[]> recentTransactions(int limit) {
        String sql =
            "SELECT d, type, ref, amt FROM ("
          + " SELECT sale_date d, 'SALE' type, invoice_number ref, total_amount amt FROM sales_header"
          + " UNION ALL SELECT receipt_date, 'RECEIVING', receipt_number, "
          + "   (SELECT COALESCE(SUM(extended_cost),0) FROM receiving_detail rd WHERE rd.receiving_id = rh.receiving_id) "
          + "   FROM receiving_header rh"
          + " UNION ALL SELECT movement_date, movement_type, COALESCE(delivery_note_number, note), extended_cost FROM movement"
          + ") t ORDER BY d DESC LIMIT " + limit;
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{ rs.getString("d"), rs.getString("type"),
                                       rs.getString("ref"), rs.getBigDecimal("amt") });
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("recentTransactions failed: " + e.getMessage(), e);
        }
    }

    public List<Object[]> lowStockAlerts(int limit) {
        String sql = "SELECT item_code, description, soh_computed FROM v_stock_on_hand "
                   + "WHERE soh_computed < 30 ORDER BY soh_computed ASC LIMIT " + limit;
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new Object[]{ rs.getString("item_code"), rs.getString("description"),
                                       rs.getBigDecimal("soh_computed") });
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("lowStockAlerts failed: " + e.getMessage(), e);
        }
    }

    private int scalarInt(String sql) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("KPI query failed: " + e.getMessage(), e);
        }
    }

    private BigDecimal scalarMoney(String sql) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException("KPI query failed: " + e.getMessage(), e);
        }
    }
}
