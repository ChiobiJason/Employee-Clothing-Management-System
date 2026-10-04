package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {

    @Test
    void accountStartsWithDefaultBalance() {
        Account account = new Account();

        assertEquals(40, account.getAccountBalance());
    }

    @Test
    void deductFromAccountBalanceReducesBalanceAndReturnsMessage() {
        Account account = new Account();
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 25, 3);

        String message = account.deductFromAccountBalance(clothing);

        assertEquals("$25 deducted from your account balance. Your new Account balance is 15.", message);
        assertEquals(15, account.getAccountBalance());
    }
}
