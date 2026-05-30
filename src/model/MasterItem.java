package model;

import java.math.BigDecimal;

public class MasterItem {
    private int itemId;
    private String itemCode;
    private String description;
    private String itemClass;
    private Integer categoryId;
    private Integer outletId;
    private String baseUnit;
    private String purchaseUnit;
    private BigDecimal qtyPerPurchaseUnit;
    private BigDecimal currentCost;
    private BigDecimal sellingPrice;
    private String status;

    private String categoryName;
    private String outletName;

    public MasterItem() {}

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getItemClass() { return itemClass; }
    public void setItemClass(String itemClass) { this.itemClass = itemClass; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public Integer getOutletId() { return outletId; }
    public void setOutletId(Integer outletId) { this.outletId = outletId; }

    public String getBaseUnit() { return baseUnit; }
    public void setBaseUnit(String baseUnit) { this.baseUnit = baseUnit; }

    public String getPurchaseUnit() { return purchaseUnit; }
    public void setPurchaseUnit(String purchaseUnit) { this.purchaseUnit = purchaseUnit; }

    public BigDecimal getQtyPerPurchaseUnit() { return qtyPerPurchaseUnit; }
    public void setQtyPerPurchaseUnit(BigDecimal qtyPerPurchaseUnit) { this.qtyPerPurchaseUnit = qtyPerPurchaseUnit; }

    public BigDecimal getCurrentCost() { return currentCost; }
    public void setCurrentCost(BigDecimal currentCost) { this.currentCost = currentCost; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getOutletName() { return outletName; }
    public void setOutletName(String outletName) { this.outletName = outletName; }
}
