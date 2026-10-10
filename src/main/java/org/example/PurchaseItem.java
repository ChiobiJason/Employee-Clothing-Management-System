package org.example;

import java.util.UUID;

public class PurchaseItem {
    private final UUID purchaseItemId;
    private final Clothing clothingItemPurchased;
    private final int quantityPurchased;
    private final int priceAtTimeOfPurchase;

    public PurchaseItem(Clothing clothingItemPurchased, int quantityPurchased, int priceAtTimeOfPurchase) {
        this.purchaseItemId = UUID.randomUUID();

        if (clothingItemPurchased == null) {
            throw new IllegalArgumentException("Clothing Item cannot be null");
        } else {
            this.clothingItemPurchased = clothingItemPurchased;
        }

        if (quantityPurchased < 1) {
            throw new IllegalArgumentException("Quantity of the Purchased Item must be greater than 0");
        } else {
            this.quantityPurchased = quantityPurchased;
        }

        if (priceAtTimeOfPurchase < 0) {
            throw new IllegalArgumentException("Price of Purchased Item cannot be negative");
        } else {
            this.priceAtTimeOfPurchase = priceAtTimeOfPurchase;
        }
    }

    public UUID getPurchaseItemId() {
        return purchaseItemId;
    }

    public Clothing getClothingItemPurchased() {
        return clothingItemPurchased;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public int getPriceAtTimeOfPurchase() {
        return priceAtTimeOfPurchase;
    }
}
