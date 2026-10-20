package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Test
    void accountStartsWithDefaultBalance() {
        Account account = new Account();

        assertEquals(4000, account.getAccountBalance());
    }

    @Test
    void deductFromAccountBalanceReducesBalanceAndReturnsMessage() {
        Account account = new Account();
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 3);

        String message = account.deductFromAccountBalance(clothing);

        assertEquals("$25.00 deducted from your account balance. Your new Account balance is $15.00.", message);
        assertEquals(1500, account.getAccountBalance());
    }

    @Test
    void deductFromAccountBalanceKeepsCentAmountsInTheMessage() {
        Account account = new Account();
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2599, 3);

        String message = account.deductFromAccountBalance(clothing);

        assertEquals("$25.99 deducted from your account balance. Your new Account balance is $14.01.", message);
        assertEquals(1401, account.getAccountBalance());
    }

    @Test
    void deductingFullBalanceBringsBalanceToZero() {
        Account account = new Account();
        Clothing clothing = new Clothing("Jacket", "Outerwear", 4000, 1);

        String message = account.deductFromAccountBalance(clothing);

        assertEquals("$40.00 deducted from your account balance. Your new Account balance is $0.00.", message);
        assertEquals(0, account.getAccountBalance());
    }

    @Test
    void deductFromAccountBalanceRejectsAPriceGreaterThanTheBalance() {
        Account account = new Account();
        Clothing clothing = new Clothing("Winter Jacket", "Outerwear", 5000, 1);

        assertThrows(IllegalArgumentException.class, () -> account.deductFromAccountBalance(clothing));
        assertEquals(4000, account.getAccountBalance());
    }

    @Test
    void deductAllowanceReducesBalanceWithoutGoingNegative() {
        Account account = new Account();

        account.deductAllowance(1500);

        assertEquals(2500, account.getAccountBalance());
        assertThrows(IllegalArgumentException.class, () -> account.deductAllowance(2600));
        assertThrows(IllegalArgumentException.class, () -> account.deductAllowance(-1));
        assertEquals(2500, account.getAccountBalance());
    }

    @Test
    void restoreDeductedAllowanceReturnsTheDeductedAmount() {
        Account account = new Account();
        account.deductAllowance(1500);

        account.restoreDeductedAllowance(1500);

        assertEquals(4000, account.getAccountBalance());
        assertThrows(IllegalArgumentException.class, () -> account.restoreDeductedAllowance(-1));
        assertEquals(4000, account.getAccountBalance());
    }
}
