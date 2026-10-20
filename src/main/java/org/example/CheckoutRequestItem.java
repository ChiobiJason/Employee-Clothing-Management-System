package org.example;

/**
 * One clothing line an employee wants to buy before the checkout is prepared.
 * The line stores the requested quantity. It does not store a purchase price.
 */
public class CheckoutRequestItem {
    private final Clothing clothingRequestedToBePurchased;
    private final int numberOfClothingRequestedToBePurchased;

    /**
     * Creates a request for a positive quantity of one clothing item.
     *
     * @param clothingRequestedToBePurchased - clothing item the employee wants
     * @param numberOfClothingRequestedToBePurchased - quantity requested
     */
    public CheckoutRequestItem(Clothing clothingRequestedToBePurchased, int numberOfClothingRequestedToBePurchased) {
        validateClothing(clothingRequestedToBePurchased);
        validateQuantity(numberOfClothingRequestedToBePurchased);
        this.clothingRequestedToBePurchased = clothingRequestedToBePurchased;
        this.numberOfClothingRequestedToBePurchased = numberOfClothingRequestedToBePurchased;
    }

    /**
     * Checks that the requested clothing item exists.
     *
     * @param clothingRequestedToBePurchased - clothing item the employee wants
     */
    private void validateClothing(Clothing clothingRequestedToBePurchased) {
        if (clothingRequestedToBePurchased == null) {
            throw new IllegalArgumentException("Clothing Item cannot be null");
        }
    } //validateClothing

    /**
     * Checks that the requested quantity is greater than zero.
     *
     * @param numberOfClothingRequestedToBePurchased - quantity requested
     */
    private void validateQuantity(int numberOfClothingRequestedToBePurchased) {
        if (numberOfClothingRequestedToBePurchased < 0 || numberOfClothingRequestedToBePurchased == 0) {
            throw new IllegalArgumentException("Quantity of Clothing Item to be purchased must be greater than zero");
        }
    } //validateQuantity

    /**
     * Returns the clothing item the employee wants to buy.
     *
     * @return - requested clothing item
     */
    public Clothing getClothingRequestedToBePurchased() {
        return clothingRequestedToBePurchased;
    } //getClothingRequestedToBePurchased

    /**
     * Returns the quantity the employee wants to buy.
     *
     * @return - requested quantity
     */
    public int getNumberOfClothingRequestedToBePurchased() {
        return numberOfClothingRequestedToBePurchased;
    } //getNumberOfClothingRequestedToBePurchased
}
