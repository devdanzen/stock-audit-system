package dao;

import db.DBConnection;
import model.Movement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovementDAO {

    public int insert(Movement m) {
        String sql = "INSERT INTO movement(item_id, destination_outlet_id, movement_date, movement_type, "
                   + "note, delivery_note_number, quantity, unit, unit_cost, extended_cost) "
                   + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, m.getItemId());
            if (m.getDestinationOutletId() == null) ps.setNull(2, java.sql.Types.INTEGER);
            else ps.setInt(2, m.getDestinationOutletId());
            ps.setDate(3, Date.valueOf(m.getMovementDate()));
            ps.setString(4, m.getMovementType());
            ps.setString(5, m.getNote());
            ps.setString(6, m.getDeliveryNoteNumber());
            ps.setBigDecimal(7, m.getQuantity());
            ps.setString(8, m.getUnit());
            ps.setBigDecimal(9, m.getUnitCost());
            ps.setBigDecimal(10, m.getExtendedCost());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Insert movement failed: " + e.getMessage(), e);
        }
    }
}
