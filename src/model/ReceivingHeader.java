package model;

import java.time.LocalDate;

public class ReceivingHeader {
    private int receivingId;
    private String receiptNumber;
    private String receiptType;
    private LocalDate receiptDate;
    private String poNumber;
    private Integer vendorId;
    private Integer outletId;
    private String postingStatus; // Pending | Approved | Posted
    private Integer postedUserId;

    public ReceivingHeader() {}

    public int getReceivingId() { return receivingId; }
    public void setReceivingId(int receivingId) { this.receivingId = receivingId; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getReceiptType() { return receiptType; }
    public void setReceiptType(String receiptType) { this.receiptType = receiptType; }

    public LocalDate getReceiptDate() { return receiptDate; }
    public void setReceiptDate(LocalDate receiptDate) { this.receiptDate = receiptDate; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }

    public Integer getOutletId() { return outletId; }
    public void setOutletId(Integer outletId) { this.outletId = outletId; }

    public String getPostingStatus() { return postingStatus; }
    public void setPostingStatus(String postingStatus) { this.postingStatus = postingStatus; }

    public Integer getPostedUserId() { return postedUserId; }
    public void setPostedUserId(Integer postedUserId) { this.postedUserId = postedUserId; }
}
