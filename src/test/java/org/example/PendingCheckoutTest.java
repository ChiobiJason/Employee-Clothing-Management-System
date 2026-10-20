package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PendingCheckoutTest {

    @Test
    void pendingCheckoutStoresCapturedAmountsAndCopiesItsItemList() {
        Employee employee = new Employee("Jane", "Doe", 101);
        PurchaseItem item = new PurchaseItem(new Clothing("Polo Shirt", "Shirt", 2500, 5), 2, 2000);
        List<PurchaseItem> items = new ArrayList<>(List.of(item));

        PendingCheckout pendingCheckout = new PendingCheckout(employee, items, 4000, 3000, 1000);
        items.clear();

        assertEquals(employee, pendingCheckout.getEmployeeWhoMadeThePurchase());
        assertEquals(List.of(item), pendingCheckout.getPurchaseItems());
        assertEquals(4000, pendingCheckout.getTotalPurchaseCost());
        assertEquals(3000, pendingCheckout.getAllowanceUsedForPurchase());
        assertEquals(1000, pendingCheckout.getAmountToBeDeductedFromPayroll());
    }

    @Test
    void pendingCheckoutItemListCannotBeModified() {
        PurchaseItem item = new PurchaseItem(new Clothing("Polo Shirt", "Shirt", 2500, 5), 1, 2500);
        PendingCheckout pendingCheckout = new PendingCheckout(
                new Employee("Jane", "Doe", 101), List.of(item), 2500, 2500, 0
        );

        assertThrows(UnsupportedOperationException.class, () -> pendingCheckout.getPurchaseItems().clear());
    }

    @Test
    void pendingCheckoutRejectsAmountsThatDoNotMatchTheCapturedPrices() {
        Employee employee = new Employee("Jane", "Doe", 101);
        PurchaseItem item = new PurchaseItem(new Clothing("Polo Shirt", "Shirt", 2500, 5), 2, 2000);

        assertThrows(IllegalArgumentException.class,
                () -> new PendingCheckout(employee, List.of(item), 5000, 4000, 1000));
        assertThrows(IllegalArgumentException.class,
                () -> new PendingCheckout(employee, List.of(item), 4000, 4000, 1000));
    }
}
