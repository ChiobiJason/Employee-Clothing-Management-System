package org.example;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * A checkout that finished successfully. The total, allowance, and payroll
 * deduction are integer cents captured from the pending checkout. The purchase
 * has no setters, and its item list cannot be changed.
 */
public class Purchase {
    private final UUID purchaseId;
    private final Employee employeeWhoMadeThePurchase;
    private final List<PurchaseItem> purchaseItems;
    private final int totalPurchaseCost;
    private final int allowanceUsedForPurchase;
    private final int amountToBeDeductedFromPayroll;
    private final Instant completedPurchaseTimestamp;

    /**
     * Records a completed purchase.
     *
     * @param employeeWhoMadeThePurchase - employee who completed the purchase
     * @param purchaseItems - purchase lines with captured prices
     * @param totalPurchaseCost - total cost in cents
     * @param allowanceUsedForPurchase - allowance used in cents
     * @param amountToBeDeductedFromPayroll - payroll deduction in cents
     * @param completedPurchaseTimestamp - time the purchase completed
     */
    public Purchase(Employee employeeWhoMadeThePurchase, List<PurchaseItem> purchaseItems,
                    int totalPurchaseCost, int allowanceUsedForPurchase,
                    int amountToBeDeductedFromPayroll, Instant completedPurchaseTimestamp) {
        this.purchaseId = UUID.randomUUID();
        this.employeeWhoMadeThePurchase = employeeWhoMadeThePurchase;
        this.purchaseItems = List.copyOf(purchaseItems);
        this.totalPurchaseCost = totalPurchaseCost;
        this.allowanceUsedForPurchase = allowanceUsedForPurchase;
        this.amountToBeDeductedFromPayroll = amountToBeDeductedFromPayroll;
        this.completedPurchaseTimestamp = completedPurchaseTimestamp;
    }

    /**
     * Returns the id of this purchase.
     *
     * @return - purchase id
     */
    public UUID getPurchaseId() {
        return purchaseId;
    } //getPurchaseId

    /**
     * Returns the employee who completed this purchase.
     *
     * @return - employee who completed the purchase
     */
    public Employee getEmployeeWhoMadeThePurchase() {
        return employeeWhoMadeThePurchase;
    } //getEmployeeWhoMadeThePurchase

    /**
     * Returns the purchase lines. The returned list cannot be modified.
     *
     * @return - purchase lines
     */
    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    } //getPurchaseItems

    /**
     * Returns the total purchase cost in cents.
     *
     * @return - total cost in cents
     */
    public int getTotalPurchaseCost() {
        return totalPurchaseCost;
    } //getTotalPurchaseCost

    /**
     * Returns the allowance used for this purchase in cents.
     *
     * @return - allowance used in cents
     */
    public int getAllowanceUsedForPurchase() {
        return allowanceUsedForPurchase;
    } //getAllowanceUsedForPurchase

    /**
     * Returns the payroll deduction for this purchase in cents.
     *
     * @return - payroll deduction in cents
     */
    public int getAmountToBeDeductedFromPayroll() {
        return amountToBeDeductedFromPayroll;
    } //getAmountToBeDeductedFromPayroll

    /**
     * Returns the time this purchase completed.
     *
     * @return - completion time
     */
    public Instant getPurchaseTimestamp() {
        return completedPurchaseTimestamp;
    } //getPurchaseTimestamp
}
