package model;

import java.math.BigDecimal;

public class ReceivingDetail {
    private int receivingDetailId;
    private int receivingId;
    private int itemId;
    private BigDecimal qtyReceived;
    private BigDecimal qtyInvoiced;
    private BigDecimal qtyReturned;
    private String unit;
    private BigDecimal unitCost;
    private BigDecimal extendedCost;

    // transient (for line table display)
    private String itemCode;
    private String description;

    public ReceivingDetail() {}

    public int getReceivingDetailId() { return receivingDetailId; }
    public void setReceivingDetailId(int receivingDetailId) { this.receivingDetailId = receivingDetailId; }

    public int getReceivingId() { return receivingId; }
    public void setReceivingId(int receivingId) { this.receivingId = receivingId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public BigDecimal getQtyReceived() { return qtyReceived; }
    public void setQtyReceived(BigDecimal qtyReceived) { this.qtyReceived = qtyReceived; }

    public BigDecimal getQtyInvoiced() { return qtyInvoiced; }
    public void setQtyInvoiced(BigDecimal qtyInvoiced) { this.qtyInvoiced = qtyInvoiced; }

    public BigDecimal getQtyReturned() { return qtyReturned; }
    public void setQtyReturned(BigDecimal qtyReturned) { this.qtyReturned = qtyReturned; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getExtendedCost() { return extendedCost; }
    public void setExtendedCost(BigDecimal extendedCost) { this.extendedCost = extendedCost; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
