package org.example;

import java.time.LocalDate;

/**
 * An employee's clothing allowance. Every amount in this class is an integer
 * number of cents. The standard allowance of $40.00 is stored as 4000.
 */
public class Account {
    public static final int STANDARD_CLOTHING_ALLOWANCE_IN_CENTS = 4000;

    private int accountBalance;

    public Account() {
        accountBalance = STANDARD_CLOTHING_ALLOWANCE_IN_CENTS;
    }

    /**
     * Returns the current allowance balance in cents.
     *
     * @return - allowance balance in cents
     */
    public int getAccountBalance() {
        return accountBalance;
    } //getAccountBalance

    /**
     * Deducts one clothing price from the allowance and describes the new balance.
     * The price is cents, and the deduction cannot make the balance negative.
     *
     * @param clothing - clothing item whose price is deducted
     * @return - message showing the deducted price and the remaining balance
     */
    public String deductFromAccountBalance(Clothing clothing) {
        int priceInCents = clothing.getItemPrice();
        deductAllowance(priceInCents);
        return formatDeductionMessage(priceInCents);
    } //deductFromAccountBalance

    /**
     * Deducts a completed purchase's allowance from this account.
     *
     * @param allowanceToDeduct - allowance amount to deduct, in cents
     */
    public void deductAllowance(int allowanceToDeduct) {
        validateAllowanceToDeduct(allowanceToDeduct);
        accountBalance = Math.subtractExact(accountBalance, allowanceToDeduct);
    } //deductAllowance

    /**
     * Adds back an allowance deduction after a checkout update fails.
     *
     * @param allowanceToRestore - allowance amount to restore, in cents
     */
    void restoreDeductedAllowance(int allowanceToRestore) {
        validateAllowanceToRestore(allowanceToRestore);
        accountBalance = Math.addExact(accountBalance, allowanceToRestore);
    } //restoreDeductedAllowance

    /**
     * Checks that an allowance deduction is not negative and does not
     * exceed the current balance.
     *
     * @param allowanceToDeduct - allowance amount to deduct, in cents
     */
    private void validateAllowanceToDeduct(int allowanceToDeduct) {
        if (allowanceToDeduct < 0) {
            throw new IllegalArgumentException("Allowance to deduct cannot be negative");
        } else if (allowanceToDeduct > accountBalance) {
            throw new IllegalArgumentException("Allowance to deduct cannot be greater than the account balance");
        }
    } //validateAllowanceToDeduct

    /**
     * Checks that a restored allowance amount is not negative.
     *
     * @param allowanceToRestore - allowance amount to restore, in cents
     */
    private void validateAllowanceToRestore(int allowanceToRestore) {
        if (allowanceToRestore < 0) {
            throw new IllegalArgumentException("Allowance to restore cannot be negative");
        }
    } //validateAllowanceToRestore

    /**
     * Builds the deduction message using dollar amounts.
     *
     * @param priceInCents - deducted clothing price, in cents
     * @return - deduction message
     */
    private String formatDeductionMessage(int priceInCents) {
        return formatCentsAsDollars(priceInCents)
                + " deducted from your account balance. Your new Account balance is "
                + formatCentsAsDollars(accountBalance) + ".";
    } //formatDeductionMessage

    /**
     * Formats an integer cent amount as a dollar string. 4000 becomes $40.00.
     *
     * @param amountInCents - amount to format, in cents
     * @return - dollar representation of the amount
     */
    public static String formatCentsAsDollars(int amountInCents) {
        int dollars = amountInCents / 100;
        int cents = Math.floorMod(amountInCents, 100);

        if (cents < 10) {
            return "$" + dollars + ".0" + cents;
        }
        return "$" + dollars + "." + cents;
    } //formatCentsAsDollars

    /**
     * Adds a new standard allowance when the date is New Year's Day.
     */
    private void addToBalance() {
        LocalDate today = LocalDate.now();

        // New Year --> Replenish Clothing Allowance
        if (today.getDayOfMonth() == 1 && today.getMonthValue() == 1) {
            accountBalance = Math.addExact(accountBalance, STANDARD_CLOTHING_ALLOWANCE_IN_CENTS);
        }
    } //addToBalance
}
