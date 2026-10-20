package org.example;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates a clothing checkout. Prices, totals, allowance, and payroll
 * deductions are integer cents. A failed completion reverses every stock and
 * allowance update made during that attempt.
 */
public class CheckoutService {
    private final Employee employeeMakingPurchase;
    private final List<CheckoutRequestItem> checkoutRequestItems;
    private final List<PendingCheckout> completedCheckouts;

    public CheckoutService(Employee employeeMakingPurchase, List<CheckoutRequestItem> checkoutRequestItems) {
        validateEmployee(employeeMakingPurchase);
        validateCheckoutRequestItems(checkoutRequestItems);
        this.employeeMakingPurchase = employeeMakingPurchase;
        this.checkoutRequestItems = List.copyOf(checkoutRequestItems);
        this.completedCheckouts = new ArrayList<>();
    }

    /**
     * Captures the prices, total, allowance, and payroll deduction for review.
     * Preparation does not change inventory or the account balance.
     *
     * @return - pending checkout showing the amounts the employee can approve
     */
    public PendingCheckout prepareCheckout() { //Wrapper
        validateStockAvailability(checkoutRequestItems);

        List<PurchaseItem> purchaseItems = createPurchaseItems(checkoutRequestItems);
        int totalCost = calculateTotal(purchaseItems);
        int allowanceUsed = calculateAllowanceUsed(employeeMakingPurchase, totalCost);
        int payrollDeduction = calculatePayrollDeduction(totalCost, allowanceUsed);
        return new PendingCheckout(employeeMakingPurchase, purchaseItems, totalCost, allowanceUsed, payrollDeduction);
    } //prepareCheckout

    /**
     * Completes the pending checkout the employee reviewed. The completed
     * purchase uses that checkout's captured prices and amounts.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     * @param employeeApprovesPayrollDeduction - true when the employee agrees
     *                                            to that checkout's paycheck deduction
     * @return - completed purchase
     */
    public Purchase completeCheckout(PendingCheckout pendingCheckout,
                                     boolean employeeApprovesPayrollDeduction) { //Wrapper
        validatePendingCheckout(pendingCheckout);
        validatePendingCheckoutBelongsToEmployee(pendingCheckout);
        validatePendingCheckoutIsOpen(pendingCheckout);
        validateStockForPurchaseItems(pendingCheckout.getPurchaseItems());
        validateAllowanceStillAvailable(pendingCheckout.getAllowanceUsedForPurchase());
        confirmPayrollDeduction(pendingCheckout.getAmountToBeDeductedFromPayroll(),
                employeeApprovesPayrollDeduction);

        return applyCheckoutUpdates(pendingCheckout);
    } //completeCheckout

    /**
     * Applies the allowance and stock updates. If any later step fails, the
     * updates from this attempt are reversed before the exception continues.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     * @return - completed purchase
     */
    private Purchase applyCheckoutUpdates(PendingCheckout pendingCheckout) {
        boolean allowanceWasDeducted = false;
        List<PurchaseItem> reducedItems = new ArrayList<>();

        try {
            allowanceWasDeducted = deductAllowanceForCheckout(pendingCheckout.getAllowanceUsedForPurchase());
            reduceStock(pendingCheckout.getPurchaseItems(), reducedItems);
            Purchase purchase = createPurchase(pendingCheckout);
            recordCompletedCheckout(pendingCheckout);
            return purchase;
        } catch (RuntimeException checkoutUpdateFailed) {
            reverseCheckoutUpdates(allowanceWasDeducted, pendingCheckout.getAllowanceUsedForPurchase(), reducedItems);
            throw checkoutUpdateFailed;
        }
    } //applyCheckoutUpdates

    /**
     * Restores stock and allowance changed by a failed checkout attempt.
     *
     * @param allowanceWasDeducted - true when the allowance deduction succeeded
     * @param allowanceUsed - allowance captured on the pending checkout, in cents
     * @param reducedItems - purchase lines whose stock was already reduced
     */
    private void reverseCheckoutUpdates(boolean allowanceWasDeducted, int allowanceUsed,
                                        List<PurchaseItem> reducedItems) {
        restoreStock(reducedItems);
        restoreAllowance(allowanceWasDeducted, allowanceUsed);
    } //reverseCheckoutUpdates

    /**
     * Checks that the employee making the purchase exists.
     *
     * @param employee - employee making the purchase
     */
    private void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
    } //validateEmployee

    /**
     * Checks that the checkout request contains at least one item.
     *
     * @param itemsToBeBought - clothing items the employee wants to purchase
     */
    private void validateCheckoutRequestItems(List<CheckoutRequestItem> itemsToBeBought) {
        if (itemsToBeBought == null || itemsToBeBought.isEmpty()) {
            throw new IllegalArgumentException("Items to be bought must be greater than zero");
        }
        validateEachCheckoutRequestItemIsPresent(itemsToBeBought);
    } //validateCheckoutRequestItems

    /**
     * Checks that every requested line refers to a checkout item.
     *
     * @param itemsToBeBought - clothing items the employee wants to purchase
     */
    private void validateEachCheckoutRequestItemIsPresent(List<CheckoutRequestItem> itemsToBeBought) {
        for (int i = 0; i < itemsToBeBought.size(); i++) {
            if (itemsToBeBought.get(i) == null) {
                throw new IllegalArgumentException("Checkout request item cannot be null");
            }
        }
    } //validateEachCheckoutRequestItemIsPresent

    /**
     * Checks that the catalogue has enough stock for every requested item.
     *
     * @param itemsToBeBought - clothing items the employee wants to purchase
     */
    private void validateStockAvailability(List<CheckoutRequestItem> itemsToBeBought) {
        for (int i = 0; i < itemsToBeBought.size(); i++) {
            Clothing clothing = itemsToBeBought.get(i).getClothingRequestedToBePurchased();
            int quantityRequested = totalQuantityRequested(clothing, itemsToBeBought);
            if (quantityRequested > clothing.getQuantityAvailable()) {
                throw new IllegalArgumentException("Not Enough Stock Available to fulfill this purchase");
            }
        }
    } //validateStockAvailability

    /**
     * Adds the requested quantity for one clothing item, including repeated lines.
     *
     * @param clothing - clothing item being counted
     * @param itemsToBeBought - clothing items the employee wants to purchase
     * @return - total quantity requested for the clothing item
     */
    private int totalQuantityRequested(Clothing clothing, List<CheckoutRequestItem> itemsToBeBought) {
        int quantityRequested = 0;

        for (int i = 0; i < itemsToBeBought.size(); i++) {
            Clothing currentClothing = itemsToBeBought.get(i).getClothingRequestedToBePurchased();
            if (currentClothing == clothing) {
                quantityRequested = Math.addExact(quantityRequested,
                        itemsToBeBought.get(i).getNumberOfClothingRequestedToBePurchased());
            }
        }

        return quantityRequested;
    } //totalQuantityRequested

    /**
     * Checks that a pending checkout was provided.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     */
    private void validatePendingCheckout(PendingCheckout pendingCheckout) {
        if (pendingCheckout == null) {
            throw new IllegalArgumentException("Pending checkout cannot be null");
        }
    } //validatePendingCheckout

    /**
     * Checks that the pending checkout belongs to this service's employee.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     */
    private void validatePendingCheckoutBelongsToEmployee(PendingCheckout pendingCheckout) {
        if (pendingCheckout.getEmployeeWhoMadeThePurchase() != employeeMakingPurchase) {
            throw new IllegalArgumentException("Pending checkout belongs to a different employee");
        }
    } //validatePendingCheckoutBelongsToEmployee

    /**
     * Checks that this pending checkout has not already been completed.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     */
    private void validatePendingCheckoutIsOpen(PendingCheckout pendingCheckout) {
        if (completedCheckouts.contains(pendingCheckout)) {
            throw new IllegalArgumentException("Pending checkout has already been completed");
        }
    } //validatePendingCheckoutIsOpen

    /**
     * Checks that the captured quantities are still available.
     *
     * @param purchaseItems - purchase lines with captured prices
     */
    private void validateStockForPurchaseItems(List<PurchaseItem> purchaseItems) {
        for (int i = 0; i < purchaseItems.size(); i++) {
            Clothing clothing = purchaseItems.get(i).getClothingItemPurchased();
            int quantityRequested = totalQuantityPurchased(clothing, purchaseItems);
            if (quantityRequested > clothing.getQuantityAvailable()) {
                throw new IllegalArgumentException("Not Enough Stock Available to fulfill this purchase");
            }
        }
    } //validateStockForPurchaseItems

    /**
     * Adds the captured quantity for one clothing item, including repeated lines.
     *
     * @param clothing - clothing item being counted
     * @param purchaseItems - purchase lines with captured prices
     * @return - total captured quantity for the clothing item
     */
    private int totalQuantityPurchased(Clothing clothing, List<PurchaseItem> purchaseItems) {
        int quantityPurchased = 0;

        for (int i = 0; i < purchaseItems.size(); i++) {
            Clothing currentClothing = purchaseItems.get(i).getClothingItemPurchased();
            if (currentClothing == clothing) {
                quantityPurchased = Math.addExact(quantityPurchased, purchaseItems.get(i).getQuantityPurchased());
            }
        }

        return quantityPurchased;
    } //totalQuantityPurchased

    /**
     * Checks that the account can still cover the allowance captured at preparation.
     *
     * @param allowanceUsed - allowance captured on the pending checkout
     */
    private void validateAllowanceStillAvailable(int allowanceUsed) {
        int employeeBalance = employeeMakingPurchase.getEmployeeAccount().getAccountBalance();
        if (allowanceUsed > employeeBalance) {
            throw new IllegalArgumentException("Account balance changed since this checkout was prepared");
        }
    } //validateAllowanceStillAvailable

    /**
     * Calculates the total cost from the prices captured on the purchase lines.
     *
     * @param purchaseItems - purchase lines with captured prices
     * @return - total cost of the checkout
     */
    private int calculateTotal(List<PurchaseItem> purchaseItems) {
        int totalCost = 0;

        for (int i = 0; i < purchaseItems.size(); i++) {
            totalCost = Math.addExact(totalCost, calculateLineCost(purchaseItems.get(i)));
        }

        return totalCost;
    } //calculateTotal

    /**
     * Calculates the cost of one captured purchase line.
     *
     * @param purchaseItem - purchase line with a captured price
     * @return - captured price multiplied by quantity
     */
    private int calculateLineCost(PurchaseItem purchaseItem) {
        return Math.multiplyExact(purchaseItem.getPriceAtTimeOfPurchase(), purchaseItem.getQuantityPurchased());
    } //calculateLineCost

    /**
     * Calculates how much of the employee's current balance can be used.
     * The allowance used cannot be greater than the account balance.
     *
     * @param employee - employee making the purchase
     * @param totalCost - total cost of the checkout
     * @return - allowance applied to the checkout
     */
    private int calculateAllowanceUsed(Employee employee, int totalCost) {
        int employeeBalance = employee.getEmployeeAccount().getAccountBalance();

        if (totalCost < employeeBalance) {
            return totalCost;
        }
        return employeeBalance;
    } //calculateAllowanceUsed

    /**
     * Calculates the paycheck deduction for costs the allowance does not cover.
     *
     * @param totalCost - total cost of the checkout
     * @param allowanceUsed - allowance applied to the checkout
     * @return - amount to deduct from the next paycheck
     */
    private int calculatePayrollDeduction(int totalCost, int allowanceUsed) { //Worker
        return totalCost - allowanceUsed;
    } //calculatePayrollDeduction

    /**
     * Requires the employee's approval when the captured payroll deduction
     * is greater than zero.
     *
     * @param payrollDeduction - captured amount to deduct from the next paycheck
     * @param employeeApprovesPayrollDeduction - true when the employee agrees
     */
    private void confirmPayrollDeduction(int payrollDeduction, boolean employeeApprovesPayrollDeduction) {
        if (payrollDeduction > 0 && !employeeApprovesPayrollDeduction) {
            throw new IllegalArgumentException(
                    "Employee must approve a payroll deduction of "
                            + Account.formatCentsAsDollars(payrollDeduction)
                            + " before the purchase can be completed");
        }
    } //confirmPayrollDeduction

    /**
     * Creates the purchase lines using the catalogue price at preparation.
     *
     * @param itemsToBeBought - clothing items the employee wants to purchase
     * @return - purchase lines for the pending checkout
     */
    private List<PurchaseItem> createPurchaseItems(List<CheckoutRequestItem> itemsToBeBought) {
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        for (int i = 0; i < itemsToBeBought.size(); i++) {
            purchaseItems.add(createPurchaseItem(itemsToBeBought.get(i)));
        }

        return purchaseItems;
    } //createPurchaseItems

    /**
     * Creates one purchase line and stores the current catalogue price.
     *
     * @param itemToBeBought - one requested clothing line
     * @return - purchase line
     */
    private PurchaseItem createPurchaseItem(CheckoutRequestItem itemToBeBought) {
        Clothing clothing = itemToBeBought.getClothingRequestedToBePurchased();
        int quantity = itemToBeBought.getNumberOfClothingRequestedToBePurchased();
        int priceAtTimeOfPurchase = clothing.getItemPrice();
        return new PurchaseItem(clothing, quantity, priceAtTimeOfPurchase);
    } //createPurchaseItem

    /**
     * Removes the captured quantities from inventory and records each line
     * that was successfully reduced.
     *
     * @param purchaseItems - purchase lines with captured prices
     * @param reducedItems - lines whose stock has been reduced
     */
    private void reduceStock(List<PurchaseItem> purchaseItems, List<PurchaseItem> reducedItems) {
        for (int i = 0; i < purchaseItems.size(); i++) {
            reduceOnePurchaseItem(purchaseItems.get(i), reducedItems);
        }
    } //reduceStock

    /**
     * Removes one captured quantity from inventory. The line is recorded
     * only after that reduction succeeds.
     *
     * @param purchaseItem - purchase line being removed from inventory
     * @param reducedItems - lines whose stock has been reduced
     */
    private void reduceOnePurchaseItem(PurchaseItem purchaseItem, List<PurchaseItem> reducedItems) {
        Clothing clothing = purchaseItem.getClothingItemPurchased();
        clothing.reduceStock(purchaseItem.getQuantityPurchased());
        reducedItems.add(purchaseItem);
    } //reduceOnePurchaseItem

    /**
     * Puts previously reduced quantities back into inventory.
     *
     * @param reducedItems - lines whose stock has been reduced
     */
    private void restoreStock(List<PurchaseItem> reducedItems) {
        for (int i = 0; i < reducedItems.size(); i++) {
            restoreOnePurchaseItem(reducedItems.get(i));
        }
    } //restoreStock

    /**
     * Puts one reduced quantity back into inventory.
     *
     * @param purchaseItem - purchase line being returned to inventory
     */
    private void restoreOnePurchaseItem(PurchaseItem purchaseItem) {
        Clothing clothing = purchaseItem.getClothingItemPurchased();
        clothing.restock(purchaseItem.getQuantityPurchased());
    } //restoreOnePurchaseItem

    /**
     * Deducts the allowance captured on the pending checkout.
     *
     * @param allowanceUsed - allowance captured on the pending checkout, in cents
     * @return - true when an allowance deduction was made
     */
    private boolean deductAllowanceForCheckout(int allowanceUsed) {
        if (allowanceUsed > 0) {
            employeeMakingPurchase.getEmployeeAccount().deductAllowance(allowanceUsed);
            return true;
        }
        return false;
    } //deductAllowanceForCheckout

    /**
     * Adds back the allowance when a checkout attempt deducted it and then failed.
     *
     * @param allowanceWasDeducted - true when the allowance deduction succeeded
     * @param allowanceUsed - allowance captured on the pending checkout, in cents
     */
    private void restoreAllowance(boolean allowanceWasDeducted, int allowanceUsed) {
        if (allowanceWasDeducted) {
            employeeMakingPurchase.getEmployeeAccount().restoreDeductedAllowance(allowanceUsed);
        }
    } //restoreAllowance

    /**
     * Creates the completed purchase from the pending checkout's captured amounts.
     *
     * @param pendingCheckout - checkout amounts shown to the employee
     * @return - completed purchase
     */
    private Purchase createPurchase(PendingCheckout pendingCheckout) {
        return new Purchase(
                pendingCheckout.getEmployeeWhoMadeThePurchase(),
                pendingCheckout.getPurchaseItems(),
                pendingCheckout.getTotalPurchaseCost(),
                pendingCheckout.getAllowanceUsedForPurchase(),
                pendingCheckout.getAmountToBeDeductedFromPayroll(),
                Instant.now()
        );
    } //createPurchase

    /**
     * Records that this pending checkout has been completed.
     *
     * @param pendingCheckout - checkout that was completed
     */
    private void recordCompletedCheckout(PendingCheckout pendingCheckout) {
        completedCheckouts.add(pendingCheckout);
    } //recordCompletedCheckout
}
