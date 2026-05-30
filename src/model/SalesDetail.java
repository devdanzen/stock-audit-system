package model;

import java.math.BigDecimal;

public class SalesDetail {
    private int salesDetailId;
    private int salesId;
    private int itemId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal extendedPrice;

    private String itemCode;
    private String description;

    public SalesDetail() {}

    public int getSalesDetailId() { return salesDetailId; }
    public void setSalesDetailId(int salesDetailId) { this.salesDetailId = salesDetailId; }

    public int getSalesId() { return salesId; }
    public void setSalesId(int salesId) { this.salesId = salesId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getExtendedPrice() { return extendedPrice; }
    public void setExtendedPrice(BigDecimal extendedPrice) { this.extendedPrice = extendedPrice; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
