package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutServiceTest {

    @Test
    void prepareCheckoutCapturesAmountsWithoutChangingBalanceOrStock() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 1500, 4);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(shirt, 2))
        );

        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();

        assertEquals(3000, pendingCheckout.getTotalPurchaseCost());
        assertEquals(3000, pendingCheckout.getAllowanceUsedForPurchase());
        assertEquals(0, pendingCheckout.getAmountToBeDeductedFromPayroll());
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(4, shirt.getQuantityAvailable());
    }

    @Test
    void completeCheckoutUsesAllowanceWhenTotalFitsInBalance() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 1500, 4);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(shirt, 2))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();

        Purchase purchase = checkoutService.completeCheckout(pendingCheckout, false);

        assertEquals(3000, purchase.getTotalPurchaseCost());
        assertEquals(3000, purchase.getAllowanceUsedForPurchase());
        assertEquals(0, purchase.getAmountToBeDeductedFromPayroll());
        assertEquals(1500, purchase.getPurchaseItems().get(0).getPriceAtTimeOfPurchase());
        assertEquals(1000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(2, shirt.getQuantityAvailable());
    }

    @Test
    void payrollDeductionRequiresEmployeeApproval() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 2500, 2);
        Clothing cap = new Clothing("Cap", "Hat", 1000, 3);
        CheckoutService checkoutService = new CheckoutService(employee, List.of(
                new CheckoutRequestItem(jacket, 2),
                new CheckoutRequestItem(cap, 1)
        ));
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();

        assertEquals(2000, pendingCheckout.getAmountToBeDeductedFromPayroll());
        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(pendingCheckout, false));
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(2, jacket.getQuantityAvailable());
        assertEquals(3, cap.getQuantityAvailable());
    }

    @Test
    void approvedPayrollDeductionCompletesPurchase() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 2500, 2);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(jacket, 2))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();

        Purchase purchase = checkoutService.completeCheckout(pendingCheckout, true);

        assertEquals(5000, purchase.getTotalPurchaseCost());
        assertEquals(4000, purchase.getAllowanceUsedForPurchase());
        assertEquals(1000, purchase.getAmountToBeDeductedFromPayroll());
        assertEquals(0, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(0, jacket.getQuantityAvailable());
    }

    @Test
    void completionUsesThePreparedAmountsAfterTheCataloguePriceChanges() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 3000, 2);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(jacket, 2))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();
        jacket.setItemPrice(5000);

        Purchase purchase = checkoutService.completeCheckout(pendingCheckout, true);

        assertEquals(2000, pendingCheckout.getAmountToBeDeductedFromPayroll());
        assertEquals(6000, purchase.getTotalPurchaseCost());
        assertEquals(4000, purchase.getAllowanceUsedForPurchase());
        assertEquals(2000, purchase.getAmountToBeDeductedFromPayroll());
        assertEquals(3000, purchase.getPurchaseItems().get(0).getPriceAtTimeOfPurchase());
        assertEquals(5000, jacket.getItemPrice());
        assertEquals(0, employee.getEmployeeAccount().getAccountBalance());
    }

    @Test
    void completionRejectsABalanceThatCanNoLongerCoverThePreparedAllowance() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 2500, 2);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(jacket, 2))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();
        employee.getEmployeeAccount().deductAllowance(1500);

        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(pendingCheckout, true));
        assertEquals(2500, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(2, jacket.getQuantityAvailable());
    }

    @Test
    void completionRejectsStockThatWasSoldAfterPreparation() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing cap = new Clothing("Cap", "Hat", 1000, 1);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(cap, 1))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();
        cap.reduceStock(1);

        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(pendingCheckout, false));
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(0, cap.getQuantityAvailable());
    }

    @Test
    void failedStockUpdateRestoresTheAllowanceAndEarlierStock() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing jacket = new Clothing("Winter Jacket", "Outerwear", 3000, 1);
        Clothing cap = new Clothing("Cap", "Hat", 3000, 1) {
            @Override
            public void reduceStock(int quantityToBeTakenFromStock) {
                throw new IllegalArgumentException("stock update failed");
            }
        };
        CheckoutService checkoutService = new CheckoutService(employee, List.of(
                new CheckoutRequestItem(jacket, 1),
                new CheckoutRequestItem(cap, 1)
        ));
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();

        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(pendingCheckout, true));
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(1, jacket.getQuantityAvailable());
        assertEquals(1, cap.getQuantityAvailable());
    }

    @Test
    void prepareCheckoutRejectsAQuantityTotalThatOverflows() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 100, Integer.MAX_VALUE);
        CheckoutService checkoutService = new CheckoutService(employee, List.of(
                new CheckoutRequestItem(shirt, Integer.MAX_VALUE),
                new CheckoutRequestItem(shirt, Integer.MAX_VALUE)
        ));

        assertThrows(ArithmeticException.class, checkoutService::prepareCheckout);
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(Integer.MAX_VALUE, shirt.getQuantityAvailable());
    }

    @Test
    void samePendingCheckoutCannotBeCompletedTwice() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 1000, 4);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(shirt, 1))
        );
        PendingCheckout pendingCheckout = checkoutService.prepareCheckout();
        checkoutService.completeCheckout(pendingCheckout, false);

        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(pendingCheckout, false));
        assertEquals(3000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(3, shirt.getQuantityAvailable());
    }

    @Test
    void completionRejectsAPendingCheckoutForADifferentEmployee() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Employee otherEmployee = new Employee("John", "Smith", 102);
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 1500, 2);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(shirt, 1))
        );
        PurchaseItem otherEmployeeItem = new PurchaseItem(shirt, 1, 1500);
        PendingCheckout otherEmployeeCheckout = new PendingCheckout(
                otherEmployee, List.of(otherEmployeeItem), 1500, 1500, 0
        );

        assertThrows(IllegalArgumentException.class,
                () -> checkoutService.completeCheckout(otherEmployeeCheckout, false));
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(2, shirt.getQuantityAvailable());
    }

    @Test
    void constructorRejectsMissingEmployeeOrItems() {
        Clothing shirt = new Clothing("Polo Shirt", "Shirt", 1500, 2);
        List<CheckoutRequestItem> items = List.of(new CheckoutRequestItem(shirt, 1));
        List<CheckoutRequestItem> itemsWithNull = new ArrayList<>();
        itemsWithNull.add(null);

        assertThrows(IllegalArgumentException.class, () -> new CheckoutService(null, items));
        assertThrows(IllegalArgumentException.class,
                () -> new CheckoutService(new Employee("Jane", "Doe", 101), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new CheckoutService(new Employee("Jane", "Doe", 101), itemsWithNull));
    }

    @Test
    void prepareCheckoutRejectsInsufficientStock() {
        Employee employee = new Employee("Jane", "Doe", 101);
        Clothing cap = new Clothing("Cap", "Hat", 1000, 1);
        CheckoutService checkoutService = new CheckoutService(
                employee, List.of(new CheckoutRequestItem(cap, 2))
        );

        assertThrows(IllegalArgumentException.class, checkoutService::prepareCheckout);
        assertEquals(4000, employee.getEmployeeAccount().getAccountBalance());
        assertEquals(1, cap.getQuantityAvailable());
    }
}
