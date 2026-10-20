package org.example;

import java.util.List;

/**
 * The checkout amounts shown to an employee before confirmation. Prices,
 * the total, the allowance, and the payroll deduction are integer cents
 * captured during preparation. Confirmation completes these same amounts.
 */
public class PendingCheckout {
    private final Employee employeeWhoMadeThePurchase;
    private final List<PurchaseItem> purchaseItems;
    private final int totalPurchaseCost;
    private final int allowanceUsedForPurchase;
    private final int amountToBeDeductedFromPayroll;

    public PendingCheckout(Employee employeeWhoMadeThePurchase, List<PurchaseItem> purchaseItems,
                           int totalPurchaseCost, int allowanceUsedForPurchase,
                           int amountToBeDeductedFromPayroll) {
        validateEmployee(employeeWhoMadeThePurchase);
        validatePurchaseItems(purchaseItems);
        validateAmountsAreNotNegative(totalPurchaseCost, allowanceUsedForPurchase, amountToBeDeductedFromPayroll);
        validateAllowanceAndPayrollMatchTotal(totalPurchaseCost, allowanceUsedForPurchase,
                amountToBeDeductedFromPayroll);
        validateTotalMatchesCapturedPrices(purchaseItems, totalPurchaseCost);

        this.employeeWhoMadeThePurchase = employeeWhoMadeThePurchase;
        this.purchaseItems = List.copyOf(purchaseItems);
        this.totalPurchaseCost = totalPurchaseCost;
        this.allowanceUsedForPurchase = allowanceUsedForPurchase;
        this.amountToBeDeductedFromPayroll = amountToBeDeductedFromPayroll;
    }

    /**
     * Checks that the employee for this checkout exists.
     *
     * @param employeeWhoMadeThePurchase - employee reviewing the checkout
     */
    private void validateEmployee(Employee employeeWhoMadeThePurchase) {
        if (employeeWhoMadeThePurchase == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
    } //validateEmployee

    /**
     * Checks that the checkout captured at least one purchase line.
     *
     * @param purchaseItems - purchase lines with captured prices
     */
    private void validatePurchaseItems(List<PurchaseItem> purchaseItems) {
        if (purchaseItems == null || purchaseItems.isEmpty()) {
            throw new IllegalArgumentException("Pending checkout must contain at least one item");
        }
        validateEachPurchaseItemIsPresent(purchaseItems);
    } //validatePurchaseItems

    /**
     * Checks that every captured purchase line exists.
     *
     * @param purchaseItems - purchase lines with captured prices
     */
    private void validateEachPurchaseItemIsPresent(List<PurchaseItem> purchaseItems) {
        for (int i = 0; i < purchaseItems.size(); i++) {
            if (purchaseItems.get(i) == null) {
                throw new IllegalArgumentException("Purchase item cannot be null");
            }
        }
    } //validateEachPurchaseItemIsPresent

    /**
     * Checks that the captured financial amounts are not negative.
     *
     * @param totalPurchaseCost - captured total cost
     * @param allowanceUsedForPurchase - captured allowance
     * @param amountToBeDeductedFromPayroll - captured payroll deduction
     */
    private void validateAmountsAreNotNegative(int totalPurchaseCost, int allowanceUsedForPurchase,
                                                int amountToBeDeductedFromPayroll) {
        if (totalPurchaseCost < 0) {
            throw new IllegalArgumentException("Total purchase cost cannot be negative");
        } else if (allowanceUsedForPurchase < 0) {
            throw new IllegalArgumentException("Allowance used cannot be negative");
        } else if (amountToBeDeductedFromPayroll < 0) {
            throw new IllegalArgumentException("Payroll deduction cannot be negative");
        }
    } //validateAmountsAreNotNegative

    /**
     * Checks that the allowance and payroll deduction add up to the total.
     *
     * @param totalPurchaseCost - captured total cost
     * @param allowanceUsedForPurchase - captured allowance
     * @param amountToBeDeductedFromPayroll - captured payroll deduction
     */
    private void validateAllowanceAndPayrollMatchTotal(int totalPurchaseCost, int allowanceUsedForPurchase,
                                                        int amountToBeDeductedFromPayroll) {
        int calculatedTotal = Math.addExact(allowanceUsedForPurchase, amountToBeDeductedFromPayroll);
        if (calculatedTotal != totalPurchaseCost) {
            throw new IllegalArgumentException(
                    "Allowance used and payroll deduction must add up to the total purchase cost");
        }
    } //validateAllowanceAndPayrollMatchTotal

    /**
     * Checks that the captured line prices produce the captured total.
     *
     * @param purchaseItems - purchase lines with captured prices
     * @param totalPurchaseCost - captured total cost
     */
    private void validateTotalMatchesCapturedPrices(List<PurchaseItem> purchaseItems, int totalPurchaseCost) {
        if (calculateTotalFromCapturedPrices(purchaseItems) != totalPurchaseCost) {
            throw new IllegalArgumentException("Total purchase cost must match the captured item prices");
        }
    } //validateTotalMatchesCapturedPrices

    /**
     * Adds the captured cost of every purchase line.
     *
     * @param purchaseItems - purchase lines with captured prices
     * @return - total of the captured line costs
     */
    private int calculateTotalFromCapturedPrices(List<PurchaseItem> purchaseItems) {
        int totalCost = 0;

        for (int i = 0; i < purchaseItems.size(); i++) {
            totalCost = Math.addExact(totalCost, calculateCapturedLineCost(purchaseItems.get(i)));
        }

        return totalCost;
    } //calculateTotalFromCapturedPrices

    /**
     * Calculates one captured line cost from its stored price and quantity.
     *
     * @param purchaseItem - purchase line with a captured price
     * @return - captured price multiplied by quantity
     */
    private int calculateCapturedLineCost(PurchaseItem purchaseItem) {
        return Math.multiplyExact(purchaseItem.getPriceAtTimeOfPurchase(), purchaseItem.getQuantityPurchased());
    } //calculateCapturedLineCost

    /**
     * Returns the employee reviewing this checkout.
     *
     * @return - employee reviewing the checkout
     */
    public Employee getEmployeeWhoMadeThePurchase() {
        return employeeWhoMadeThePurchase;
    } //getEmployeeWhoMadeThePurchase

    /**
     * Returns the captured purchase lines. The returned list cannot be modified.
     *
     * @return - purchase lines with captured prices
     */
    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    } //getPurchaseItems

    /**
     * Returns the captured total cost in cents.
     *
     * @return - total cost in cents
     */
    public int getTotalPurchaseCost() {
        return totalPurchaseCost;
    } //getTotalPurchaseCost

    /**
     * Returns the captured allowance in cents.
     *
     * @return - allowance in cents
     */
    public int getAllowanceUsedForPurchase() {
        return allowanceUsedForPurchase;
    } //getAllowanceUsedForPurchase

    /**
     * Returns the captured payroll deduction in cents.
     *
     * @return - payroll deduction in cents
     */
    public int getAmountToBeDeductedFromPayroll() {
        return amountToBeDeductedFromPayroll;
    } //getAmountToBeDeductedFromPayroll
}
