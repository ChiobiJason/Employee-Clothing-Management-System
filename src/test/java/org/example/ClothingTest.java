package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
