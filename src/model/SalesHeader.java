package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesHeader {
    private int salesId;
    private String invoiceNumber;
    private LocalDate saleDate;
    private Integer outletId;
    private BigDecimal totalAmount;

    public SalesHeader() {}

    public int getSalesId() { return salesId; }
    public void setSalesId(int salesId) { this.salesId = salesId; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }

    public Integer getOutletId() { return outletId; }
    public void setOutletId(Integer outletId) { this.outletId = outletId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
