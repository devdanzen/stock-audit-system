package dao;

import db.DBConnection;
import model.MasterItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MasterItemDAO {

    private static final String SELECT =
        "SELECT mi.*, c.category_name, o.outlet_name FROM master_item mi "
      + "LEFT JOIN category c ON c.category_id = mi.category_id "
      + "LEFT JOIN outlet o ON o.outlet_id = mi.outlet_id ";

    public List<MasterItem> findAll() {
        return query(SELECT + "ORDER BY mi.item_code", null);
    }

    public List<MasterItem> findActive() {
        return query(SELECT + "WHERE mi.status = 'Active' ORDER BY mi.item_code", null);
    }

    public List<MasterItem> search(String keyword) {
        String like = "%" + keyword + "%";
        return query(SELECT + "WHERE mi.item_code LIKE ? OR mi.description LIKE ? ORDER BY mi.item_code",
                new String[]{like, like});
    }

    public MasterItem findById(int id) {
        List<MasterItem> l = query(SELECT + "WHERE mi.item_id = ?", new String[]{String.valueOf(id)});
        return l.isEmpty() ? null : l.get(0);
    }

    public int insert(MasterItem it) {
        String sql = "INSERT INTO master_item(item_code, description, item_class, category_id, outlet_id, "
                   + "base_unit, purchase_unit, qty_per_purchase_unit, current_cost, selling_price, status) "
                   + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, it);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Insert item failed: " + e.getMessage(), e);
        }
    }

    public boolean update(MasterItem it) {
        String sql = "UPDATE master_item SET item_code=?, description=?, item_class=?, category_id=?, outlet_id=?, "
                   + "base_unit=?, purchase_unit=?, qty_per_purchase_unit=?, current_cost=?, selling_price=?, status=? "
                   + "WHERE item_id=?";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, it);
            ps.setInt(12, it.getItemId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Update item failed: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM master_item WHERE item_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete item failed: " + e.getMessage(), e);
        }
    }

    private void bind(PreparedStatement ps, MasterItem it) throws SQLException {
        ps.setString(1, it.getItemCode());
        ps.setString(2, it.getDescription());
        ps.setString(3, it.getItemClass());
        if (it.getCategoryId() == null) ps.setNull(4, java.sql.Types.INTEGER); else ps.setInt(4, it.getCategoryId());
        if (it.getOutletId() == null) ps.setNull(5, java.sql.Types.INTEGER); else ps.setInt(5, it.getOutletId());
        ps.setString(6, it.getBaseUnit());
        ps.setString(7, it.getPurchaseUnit());
        ps.setBigDecimal(8, nz(it.getQtyPerPurchaseUnit(), BigDecimal.ONE));
        ps.setBigDecimal(9, nz(it.getCurrentCost(), BigDecimal.ZERO));
        ps.setBigDecimal(10, nz(it.getSellingPrice(), BigDecimal.ZERO));
        ps.setString(11, it.getStatus() == null ? "Active" : it.getStatus());
    }

    private BigDecimal nz(BigDecimal v, BigDecimal def) { return v == null ? def : v; }

    private List<MasterItem> query(String sql, String[] params) {
        List<MasterItem> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) ps.setString(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Query item failed: " + e.getMessage(), e);
        }
    }

    private MasterItem map(ResultSet rs) throws SQLException {
        MasterItem it = new MasterItem();
        it.setItemId(rs.getInt("item_id"));
        it.setItemCode(rs.getString("item_code"));
        it.setDescription(rs.getString("description"));
        it.setItemClass(rs.getString("item_class"));
        int cat = rs.getInt("category_id"); it.setCategoryId(rs.wasNull() ? null : cat);
        int out = rs.getInt("outlet_id");   it.setOutletId(rs.wasNull() ? null : out);
        it.setBaseUnit(rs.getString("base_unit"));
        it.setPurchaseUnit(rs.getString("purchase_unit"));
        it.setQtyPerPurchaseUnit(rs.getBigDecimal("qty_per_purchase_unit"));
        it.setCurrentCost(rs.getBigDecimal("current_cost"));
        it.setSellingPrice(rs.getBigDecimal("selling_price"));
        it.setStatus(rs.getString("status"));
        it.setCategoryName(rs.getString("category_name"));
        it.setOutletName(rs.getString("outlet_name"));
        return it;
    }
}
