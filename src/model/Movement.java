package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movement {
    private int movementId;
    private int itemId;
    private Integer destinationOutletId; // only for OUT
    private LocalDate movementDate;
    private String movementType; // IN | OUT | WASTE | CONSUMPTION
    private String note;
    private String deliveryNoteNumber;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitCost;
    private BigDecimal extendedCost;

    public Movement() {}

    public int getMovementId() { return movementId; }
    public void setMovementId(int movementId) { this.movementId = movementId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public Integer getDestinationOutletId() { return destinationOutletId; }
    public void setDestinationOutletId(Integer destinationOutletId) { this.destinationOutletId = destinationOutletId; }

    public LocalDate getMovementDate() { return movementDate; }
    public void setMovementDate(LocalDate movementDate) { this.movementDate = movementDate; }

    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getDeliveryNoteNumber() { return deliveryNoteNumber; }
    public void setDeliveryNoteNumber(String deliveryNoteNumber) { this.deliveryNoteNumber = deliveryNoteNumber; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getExtendedCost() { return extendedCost; }
    public void setExtendedCost(BigDecimal extendedCost) { this.extendedCost = extendedCost; }
}
