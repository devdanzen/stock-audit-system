package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Audit {
    private int auditId;
    private int itemId;
    private Integer categoryId;
    private LocalDate auditDate;
    private BigDecimal auditQuantity;
    private BigDecimal variance;
    private String note;

    public Audit() {}

    public int getAuditId() { return auditId; }
    public void setAuditId(int auditId) { this.auditId = auditId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public LocalDate getAuditDate() { return auditDate; }
    public void setAuditDate(LocalDate auditDate) { this.auditDate = auditDate; }

    public BigDecimal getAuditQuantity() { return auditQuantity; }
    public void setAuditQuantity(BigDecimal auditQuantity) { this.auditQuantity = auditQuantity; }

    public BigDecimal getVariance() { return variance; }
    public void setVariance(BigDecimal variance) { this.variance = variance; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
