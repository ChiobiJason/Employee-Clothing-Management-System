package org.example;

import java.util.UUID;

/**
 * One line in a checkout or completed purchase. The price is the catalogue
 * price in cents at the time the line was created, so a later catalogue change
 * does not change this line.
 */
public class PurchaseItem {
    private final UUID purchaseItemId;
    private final Clothing clothingItemPurchased;
    private final int quantityPurchased;
    private final int priceAtTimeOfPurchase;

    /**
     * Creates a purchase line and captures its price in cents.
     *
     * @param clothingItemPurchased - clothing item on this line
     * @param quantityPurchased - quantity purchased
     * @param priceAtTimeOfPurchase - catalogue price in cents captured for this line
     */
    public PurchaseItem(Clothing clothingItemPurchased, int quantityPurchased, int priceAtTimeOfPurchase) {
        this.purchaseItemId = UUID.randomUUID();
        validateClothing(clothingItemPurchased);
        validateQuantity(quantityPurchased);
        validatePrice(priceAtTimeOfPurchase);
        this.clothingItemPurchased = clothingItemPurchased;
        this.quantityPurchased = quantityPurchased;
        this.priceAtTimeOfPurchase = priceAtTimeOfPurchase;
    }

    /**
     * Checks that the clothing item exists.
     *
     * @param clothingItemPurchased - clothing item on this line
     */
    private void validateClothing(Clothing clothingItemPurchased) {
        if (clothingItemPurchased == null) {
            throw new IllegalArgumentException("Clothing Item cannot be null");
        }
    } //validateClothing

    /**
     * Checks that the purchased quantity is greater than zero.
     *
     * @param quantityPurchased - quantity purchased
     */
    private void validateQuantity(int quantityPurchased) {
        if (quantityPurchased < 1) {
            throw new IllegalArgumentException("Quantity of the Purchased Item must be greater than 0");
        }
    } //validateQuantity

    /**
     * Checks that the captured price is not negative.
     *
     * @param priceAtTimeOfPurchase - captured price in cents
     */
    private void validatePrice(int priceAtTimeOfPurchase) {
        if (priceAtTimeOfPurchase < 0) {
            throw new IllegalArgumentException("Price of Purchased Item cannot be negative");
        }
    } //validatePrice

    /**
     * Returns the id of this purchase line.
     *
     * @return - purchase line id
     */
    public UUID getPurchaseItemId() {
        return purchaseItemId;
    } //getPurchaseItemId

    /**
     * Returns the clothing item on this line.
     *
     * @return - clothing item
     */
    public Clothing getClothingItemPurchased() {
        return clothingItemPurchased;
    } //getClothingItemPurchased

    /**
     * Returns the quantity on this line.
     *
     * @return - quantity purchased
     */
    public int getQuantityPurchased() {
        return quantityPurchased;
    } //getQuantityPurchased

    /**
     * Returns the price captured for this line, in cents.
     *
     * @return - captured price in cents
     */
    public int getPriceAtTimeOfPurchase() {
        return priceAtTimeOfPurchase;
    } //getPriceAtTimeOfPurchase
}
