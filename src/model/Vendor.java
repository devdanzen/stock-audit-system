package model;

public class Vendor {
    private int vendorId;
    private String vendorCode;
    private String vendorName;
    private String contactPhone;
    private String status; // Active | Inactive
    private int totalReceipts; // transient, for table display

    public Vendor() {}

    public int getVendorId() { return vendorId; }
    public void setVendorId(int vendorId) { this.vendorId = vendorId; }

    public String getVendorCode() { return vendorCode; }
    public void setVendorCode(String vendorCode) { this.vendorCode = vendorCode; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalReceipts() { return totalReceipts; }
    public void setTotalReceipts(int totalReceipts) { this.totalReceipts = totalReceipts; }
}
