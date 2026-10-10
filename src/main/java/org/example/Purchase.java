package org.example;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Purchase {
    private final UUID purchaseId;
    private final Employee employeeWhoMadeThePurchase;
    private final List<PurchaseItem> purchaseItems;
    private final int totalPurchaseCost;
    private final int allowanceUsedForPurchase;
    private final int amountToBeDeductedFromPayroll;
    private final Instant completedPurchaseTimestamp;

    public Purchase(Employee employeeWhoMadeThePurchase, List<PurchaseItem> purchaseItems,
                    int totalPurchaseCost, int allowanceUsedForPurchase,
                    int amountToBeDeductedFromPayroll, Instant completedPurchaseTimestamp) {
        this.purchaseId = UUID.randomUUID();
        this.employeeWhoMadeThePurchase = employeeWhoMadeThePurchase;
        this.purchaseItems = purchaseItems;
        this.totalPurchaseCost = totalPurchaseCost;
        this.allowanceUsedForPurchase = allowanceUsedForPurchase;
        this.amountToBeDeductedFromPayroll = amountToBeDeductedFromPayroll;
        this.completedPurchaseTimestamp = completedPurchaseTimestamp;
    }

    public UUID getPurchaseId() {
        return purchaseId;
    }

    public Employee getEmployeeWhoMadeThePurchase() {
        return employeeWhoMadeThePurchase;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public int getTotalPurchaseCost() {
        return totalPurchaseCost;
    }

    public int getAllowanceUsedForPurchase() {
        return allowanceUsedForPurchase;
    }

    public int getAmountToBeDeductedFromPayroll() {
        return amountToBeDeductedFromPayroll;
    }

    public Instant getPurchaseTimestamp() {
        return completedPurchaseTimestamp;
    }
}
