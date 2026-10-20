package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PurchaseItemTest {

    @Test
    void purchaseItemStoresProvidedValues() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        PurchaseItem item = new PurchaseItem(clothing, 2, 2000);

        assertNotNull(item.getPurchaseItemId());
        assertEquals(clothing, item.getClothingItemPurchased());
        assertEquals(2, item.getQuantityPurchased());
        assertEquals(2000, item.getPriceAtTimeOfPurchase());
    }

    @Test
    void purchaseItemRejectsNullClothing() {
        assertThrows(IllegalArgumentException.class, () -> new PurchaseItem(null, 1, 20));
    }

    @Test
    void purchaseItemRejectsZeroOrNegativeQuantity() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        assertThrows(IllegalArgumentException.class, () -> new PurchaseItem(clothing, 0, 2000));
        assertThrows(IllegalArgumentException.class, () -> new PurchaseItem(clothing, -1, 2000));
    }

    @Test
    void purchaseItemRejectsNegativePurchasePrice() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        assertThrows(IllegalArgumentException.class, () -> new PurchaseItem(clothing, 1, -1));
    }
}
