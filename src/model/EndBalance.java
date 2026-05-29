package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EndBalance {
    private int endBalanceId;
    private int itemId;
    private LocalDate periodDate;
    private BigDecimal endBalance;
    private BigDecimal unitCost;
    private BigDecimal extendedCost;

    public EndBalance() {}

    public int getEndBalanceId() { return endBalanceId; }
    public void setEndBalanceId(int endBalanceId) { this.endBalanceId = endBalanceId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public LocalDate getPeriodDate() { return periodDate; }
    public void setPeriodDate(LocalDate periodDate) { this.periodDate = periodDate; }

    public BigDecimal getEndBalance() { return endBalance; }
    public void setEndBalance(BigDecimal endBalance) { this.endBalance = endBalance; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getExtendedCost() { return extendedCost; }
    public void setExtendedCost(BigDecimal extendedCost) { this.extendedCost = extendedCost; }
}
