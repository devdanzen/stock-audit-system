package dao;

import db.DBConnection;
import model.MasterItem;
import model.RecipeDetail;
import model.RecipeHeader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    /** Atomic save: one recipe_header + N recipe_detail (ingredients). */
    public int insertWithDetails(RecipeHeader h, List<RecipeDetail> lines) {
        String hSql = "INSERT INTO recipe_header(recipe_code, item_id, item_class) VALUES (?,?,?)";
        String dSql = "INSERT INTO recipe_detail(recipe_id, item_id, initial_weight, final_weight, "
                    + "waste_percentage, unit) VALUES (?,?,?,?,?,?)";
        Connection c = null;
        try {
            c = DBConnection.connect();
            c.setAutoCommit(false);

            int recipeId;
            try (PreparedStatement ps = c.prepareStatement(hSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, h.getRecipeCode());
                ps.setInt(2, h.getItemId());
                ps.setString(3, h.getItemClass());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    recipeId = keys.next() ? keys.getInt(1) : 0;
                }
            }

            try (PreparedStatement ps = c.prepareStatement(dSql)) {
                for (RecipeDetail d : lines) {
                    ps.setInt(1, recipeId);
                    ps.setInt(2, d.getItemId());
                    ps.setBigDecimal(3, d.getInitialWeight());
                    ps.setBigDecimal(4, d.getFinalWeight());
                    ps.setBigDecimal(5, d.getWastePercentage());
                    ps.setString(6, d.getUnit());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            c.commit();
            return recipeId;
        } catch (SQLException e) {
            if (c != null) try { c.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Save recipe failed (rolled back): " + e.getMessage(), e);
        } finally {
            if (c != null) try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) {}
        }
    }

    /** Active items that do NOT already have a recipe (candidates for a finished item). */
    public List<MasterItem> findItemsWithoutRecipe() {
        String sql = "SELECT mi.* FROM master_item mi "
                   + "WHERE mi.status='Active' AND mi.item_id NOT IN (SELECT item_id FROM recipe_header) "
                   + "ORDER BY mi.item_code";
        List<MasterItem> list = new ArrayList<>();
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MasterItem it = new MasterItem();
                it.setItemId(rs.getInt("item_id"));
                it.setItemCode(rs.getString("item_code"));
                it.setDescription(rs.getString("description"));
                it.setItemClass(rs.getString("item_class"));
                list.add(it);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findItemsWithoutRecipe failed: " + e.getMessage(), e);
        }
    }

    public String getNextRecipeCode() {
        int year = LocalDate.now().getYear();
        String prefix = "RCP-" + year + "-";
        String sql = "SELECT recipe_code FROM recipe_header WHERE recipe_code LIKE ? "
                   + "ORDER BY recipe_code DESC LIMIT 1";
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
                return prefix + String.format("%04d", next);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Generate recipe code failed: " + e.getMessage(), e);
        }
    }
}
