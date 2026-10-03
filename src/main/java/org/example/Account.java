package org.example;

import java.time.LocalDate;

public class Account {
    int accountBalance;

    public Account() {
        accountBalance = 40;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public String deductFromAccountBalance(Clothing clothing) {
        accountBalance -= clothing.itemPrice;
        return ("$" + clothing.itemPrice +
                " deducted from your account balance. Your new Account balance is " +
                accountBalance + ".");
    }

    private void addToBalance() {
        LocalDate today = LocalDate.now();

        // New Year --> Replenish Clothing Allowance
        if (today.getDayOfMonth() == 1 && today.getMonthValue() == 1) {
            accountBalance += 40;
        }
    }
}
