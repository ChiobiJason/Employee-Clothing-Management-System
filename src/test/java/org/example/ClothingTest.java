package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClothingTest {

    @Test
    void clothingStoresProvidedValues() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        assertEquals("Polo Shirt", clothing.getItemName());
        assertEquals("Shirt", clothing.getItemType());
        assertEquals(2500, clothing.getItemPrice());
        assertEquals(5, clothing.getQuantityAvailable());
    }

    @Test
    void settersUpdateClothingValues() {
        Clothing clothing = new Clothing("Old", "OldType", 1000, 1);

        clothing.setItemName("New Name");
        clothing.setItemType("New Type");
        clothing.setItemPrice(4000);

        assertEquals("New Name", clothing.getItemName());
        assertEquals("New Type", clothing.getItemType());
        assertEquals(4000, clothing.getItemPrice());
        assertEquals(1, clothing.getQuantityAvailable());
    }

    @Test
    void constructorRejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new Clothing(null, "Shirt", 2500, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("", "Shirt", 2500, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", null, 2500, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "", 2500, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "Shirt", -1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "Shirt", 2500, -1));
    }

    @Test
    void settersRejectInvalidNameTypeAndPrice() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        assertThrows(IllegalArgumentException.class, () -> clothing.setItemName(null));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemName(""));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemType(null));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemType(""));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemPrice(-1));
    }

    @Test
    void restockAndReduceStockChangeTheAvailableQuantity() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        clothing.restock(3);
        assertEquals(8, clothing.getQuantityAvailable());

        clothing.reduceStock(2);
        assertEquals(6, clothing.getQuantityAvailable());
    }

    @Test
    void stockOperationsRejectZeroNegativeAndExcessiveQuantities() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 2500, 5);

        assertThrows(IllegalArgumentException.class, () -> clothing.restock(0));
        assertThrows(IllegalArgumentException.class, () -> clothing.restock(-1));
        assertThrows(IllegalArgumentException.class, () -> clothing.reduceStock(0));
        assertThrows(IllegalArgumentException.class, () -> clothing.reduceStock(-1));
        assertThrows(IllegalArgumentException.class, () -> clothing.reduceStock(6));
        assertEquals(5, clothing.getQuantityAvailable());
    }
}
