package org.example;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Purchase {
    UUID purchaseId;
    Employee employeeWhoMadeThePurchase;
    List<PurchaseItem> purchaseItems;
    int totalPurchaseCost;
    int allowanceUsedForPurchase;
    int amountToBeDeductedFromPayroll;
    Instant completedPurchaseTimestamp;

    public Purchase(Employee employeeWhoMadeThePurchase, List<PurchaseItem> purchaseItems) {
        this.purchaseId = UUID.randomUUID();
        this.employeeWhoMadeThePurchase = employeeWhoMadeThePurchase;
        this.purchaseItems = purchaseItems;
    }

    public void setTotalPurchaseCost(int totalPurchaseCost) {
        this.totalPurchaseCost = totalPurchaseCost;
    }

    public void setAllowanceUsedForPurchase(int allowanceUsedForPurchase) {
        this.allowanceUsedForPurchase = allowanceUsedForPurchase;
    }

    public void setAmountToBeDeductedFromPayroll(int amountToBeDeductedFromPayroll) {
        this.amountToBeDeductedFromPayroll = amountToBeDeductedFromPayroll;
    }

    public void setCompletedPurchaseTimestamp(Instant completedPurchaseTimestamp) {
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
