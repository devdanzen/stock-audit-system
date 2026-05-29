package model;

import java.math.BigDecimal;

/** Read-only row from v_stock_on_hand joined with master_item. */
public class StockOnHandRow {
    private int itemId;
    private String itemCode;
    private String description;
    private String categoryName;
    private String outletName;
    private String baseUnit;
    private BigDecimal onHand;     // soh_computed
    private BigDecimal unitCost;   // master_item.current_cost
    private BigDecimal stockValue; // onHand * unitCost

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getOutletName() { return outletName; }
    public void setOutletName(String outletName) { this.outletName = outletName; }

    public String getBaseUnit() { return baseUnit; }
    public void setBaseUnit(String baseUnit) { this.baseUnit = baseUnit; }

    public BigDecimal getOnHand() { return onHand; }
    public void setOnHand(BigDecimal onHand) { this.onHand = onHand; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getStockValue() { return stockValue; }
    public void setStockValue(BigDecimal stockValue) { this.stockValue = stockValue; }
}
