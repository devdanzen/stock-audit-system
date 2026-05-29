package model;

public class Outlet {
    private int outletId;
    private String outletCode;
    private String outletName;
    private String status; // Active | Inactive
    private int itemCount;  // transient, for table display

    public Outlet() {}

    public int getOutletId() { return outletId; }
    public void setOutletId(int outletId) { this.outletId = outletId; }

    public String getOutletCode() { return outletCode; }
    public void setOutletCode(String outletCode) { this.outletCode = outletCode; }

    public String getOutletName() { return outletName; }
    public void setOutletName(String outletName) { this.outletName = outletName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}
