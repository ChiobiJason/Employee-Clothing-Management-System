package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClothingTest {

    @Test
    void clothingStoresProvidedValues() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 25, 5);

        assertEquals("Polo Shirt", clothing.getItemName());
        assertEquals("Shirt", clothing.getItemType());
        assertEquals(25, clothing.getItemPrice());
        assertEquals(5, clothing.getQuantityAvailable());
    }

    @Test
    void settersUpdateClothingValues() {
        Clothing clothing = new Clothing("Old", "OldType", 10, 1);

        clothing.setItemName("New Name");
        clothing.setItemType("New Type");
        clothing.setItemPrice(40);
        clothing.setQuantityAvailable(7);

        assertEquals("New Name", clothing.getItemName());
        assertEquals("New Type", clothing.getItemType());
        assertEquals(40, clothing.getItemPrice());
        assertEquals(7, clothing.getQuantityAvailable());
    }

    @Test
    void constructorRejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new Clothing(null, "Shirt", 25, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("", "Shirt", 25, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", null, 25, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "", 25, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "Shirt", -1, 5));
        assertThrows(IllegalArgumentException.class, () -> new Clothing("Polo Shirt", "Shirt", 25, -1));
    }

    @Test
    void settersRejectInvalidNameTypeAndPrice() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 25, 5);

        assertThrows(IllegalArgumentException.class, () -> clothing.setItemName(null));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemName(""));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemType(null));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemType(""));
        assertThrows(IllegalArgumentException.class, () -> clothing.setItemPrice(-1));
    }

    @Test
    void updateQuantityAddsAndDeductsStock() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 25, 5);

        clothing.updateQuantityAvailable("+", 3);
        assertEquals(8, clothing.getQuantityAvailable());

        clothing.updateQuantityAvailable("-", 2);
        assertEquals(6, clothing.getQuantityAvailable());
    }

    @Test
    void updateQuantityRejectsUnsupportedOperationsAndNegativeAmounts() {
        Clothing clothing = new Clothing("Polo Shirt", "Shirt", 25, 5);

        assertThrows(IllegalArgumentException.class, () -> clothing.updateQuantityAvailable("*", 1));
        assertThrows(IllegalArgumentException.class, () -> clothing.updateQuantityAvailable(null, 1));
        assertThrows(IllegalArgumentException.class, () -> clothing.updateQuantityAvailable("+", -1));
        assertEquals(5, clothing.getQuantityAvailable());
    }
}
