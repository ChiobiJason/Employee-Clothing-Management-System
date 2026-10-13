package org.example;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseTest {

    @Test
    void purchaseStoresProvidedValuesAndCopiesItsItemList() {
        Employee employee = new Employee("Jane", "Doe", 101);
        PurchaseItem item = new PurchaseItem(new Clothing("Polo Shirt", "Shirt", 25, 5), 2, 20);
        List<PurchaseItem> items = new ArrayList<>(List.of(item));
        Instant timestamp = Instant.parse("2026-10-10T12:00:00Z");

        Purchase purchase = new Purchase(employee, items, 40, 30, 10, timestamp);
        items.clear();

        assertNotNull(purchase.getPurchaseId());
        assertEquals(employee, purchase.getEmployeeWhoMadeThePurchase());
        assertEquals(List.of(item), purchase.getPurchaseItems());
        assertEquals(40, purchase.getTotalPurchaseCost());
        assertEquals(30, purchase.getAllowanceUsedForPurchase());
        assertEquals(10, purchase.getAmountToBeDeductedFromPayroll());
        assertEquals(timestamp, purchase.getPurchaseTimestamp());
    }

    @Test
    void purchaseItemListCannotBeModified() {
        PurchaseItem item = new PurchaseItem(new Clothing("Polo Shirt", "Shirt", 25, 5), 1, 25);
        Purchase purchase = new Purchase(
                new Employee("Jane", "Doe", 101), List.of(item), 25, 25, 0, Instant.now()
        );

        assertThrows(UnsupportedOperationException.class, () -> purchase.getPurchaseItems().clear());
    }
}
