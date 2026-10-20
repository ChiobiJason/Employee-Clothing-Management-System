package org.example;

/**
 * A catalogue clothing item. The price is an integer number of cents, so
 * $25.00 is stored as 2500. Stock changes only through restock and reduceStock.
 */
public class Clothing {
    private String itemName;
    private String itemType;
    private int itemPrice;
    private int quantityAvailable;

    /**
     * Creates a catalogue item.
     *
     * @param itemName - clothing name
     * @param itemType - clothing type
     * @param itemPrice - clothing price in cents
     * @param quantityAvailable - starting quantity in stock
     */
    public Clothing(String itemName, String itemType, int itemPrice, int quantityAvailable) {
        setItemName(itemName);
        setItemType(itemType);
        setItemPrice(itemPrice);
        setQuantityAvailable(quantityAvailable);
    }

    /**
     * Sets the clothing name. The name must contain a visible character.
     *
     * @param itemName - clothing name
     */
    public void setItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Clothing Item name cannot be null or blank");
        } else {
            this.itemName = itemName;
        }
    } //setItemName

    /**
     * Sets the clothing type. The type must contain a visible character.
     *
     * @param itemType - clothing type
     */
    public void setItemType(String itemType) {
        if (itemType == null || itemType.isBlank()) {
            throw new IllegalArgumentException("Clothing Item type cannot be null or blank");
        } else {
            this.itemType = itemType;
        }
    } //setItemType

    /**
     * Sets the clothing price in cents. The price cannot be negative.
     *
     * @param itemPrice - clothing price in cents
     */
    public void setItemPrice(int itemPrice) {
        if (itemPrice < 0) {
            throw new IllegalArgumentException("Clothing Item price cannot be negative");
        } else {
            this.itemPrice = itemPrice;
        }
    } //setItemPrice

    /**
     * Sets the starting stock. Stock cannot be negative.
     *
     * @param quantityAvailable - starting quantity in stock
     */
    private void setQuantityAvailable(int quantityAvailable) {
        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Clothing Item quantity cannot be negative");
        } else {
            this.quantityAvailable = quantityAvailable;
        }
    } //setQuantityAvailable

    /**
     * Rejects a zero or negative stock change.
     *
     * @param quantity - quantity being added or removed
     * @param operation - stock operation name used in the exception message
     */
    private void zeroNegativeCheck(int quantity, String operation) {
        if (quantity == 0)  {
            throw new IllegalArgumentException("Not Possible to " +  operation + " by 0");
        } else if (quantity < 0) {
            throw new IllegalArgumentException(operation.toUpperCase() +  " amount must be positive");
        }
    } //zeroNegativeCheck

    /**
     * Adds stock. The quantity must be positive, and the result cannot overflow.
     *
     * @param quantityToBeAddedToStock - quantity to add
     */
    public void restock(int quantityToBeAddedToStock) {
        zeroNegativeCheck(quantityToBeAddedToStock, "restock");
        quantityAvailable = Math.addExact(quantityAvailable, quantityToBeAddedToStock);
    } //restock

    /**
     * Removes stock. The quantity must be positive and no greater than the
     * quantity available.
     *
     * @param quantityToBeTakenFromStock - quantity to remove
     */
    public void reduceStock(int quantityToBeTakenFromStock) {
        zeroNegativeCheck(quantityToBeTakenFromStock, "reduce stock");
        validateEnoughStock(quantityToBeTakenFromStock);
        quantityAvailable -= quantityToBeTakenFromStock;
    } //reduceStock

    /**
     * Checks that a stock reduction does not exceed the quantity available.
     *
     * @param quantityToBeTakenFromStock - quantity to remove
     */
    private void validateEnoughStock(int quantityToBeTakenFromStock) {
        if (quantityToBeTakenFromStock > quantityAvailable) {
            throw new IllegalArgumentException("Not Enough Stock Available to fulfill this purchase");
        }
    } //validateEnoughStock

    /**
     * Returns the clothing name.
     *
     * @return - clothing name
     */
    public String getItemName() {
        return itemName;
    } //getItemName

    /**
     * Returns the clothing type.
     *
     * @return - clothing type
     */
    public String getItemType() {
        return itemType;
    } //getItemType

    /**
     * Returns the clothing price in cents.
     *
     * @return - clothing price in cents
     */
    public int getItemPrice() {
        return itemPrice;
    } //getItemPrice

    /**
     * Returns the quantity currently in stock.
     *
     * @return - quantity in stock
     */
    public int getQuantityAvailable() {
        return quantityAvailable;
    } //getQuantityAvailable
}
