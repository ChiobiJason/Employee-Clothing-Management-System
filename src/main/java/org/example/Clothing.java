package org.example;

public class Clothing {
    private String itemName;
    private String itemType;
    private int itemPrice;
    private int quantityAvailable;

    public Clothing(String itemName, String itemType, int itemPrice, int quantityAvailable) {
        setItemName(itemName);
        setItemType(itemType);
        setItemPrice(itemPrice);

        if (quantityAvailable < 0) {
            throw new IllegalArgumentException("Clothing Item quantity cannot be negative");
        } else {
            this.quantityAvailable = quantityAvailable;
        }
    }

    public void setItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Clothing Item name cannot be null or blank");
        } else {
            this.itemName = itemName;
        }
    }

    public void setItemType(String itemType) {
        if (itemType == null || itemType.isBlank()) {
            throw new IllegalArgumentException("Clothing Item type cannot be null or blank");
        } else {
            this.itemType = itemType;
        }
    }

    public void setItemPrice(int itemPrice) {
        if (itemPrice < 0) {
            throw new IllegalArgumentException("Clothing Item price cannot be negative");
        } else {
            this.itemPrice = itemPrice;
        }
    }

    private void zeroNegativeCheck(int quantity, String operation) {
        if (quantity == 0)  {
            throw new IllegalArgumentException("Not Possible to " +  operation + " by 0");
        } else if (quantity < 0) {
            throw new IllegalArgumentException(operation.toUpperCase() +  " amount must be positive");
        }
    }

    public void restock(int quantityToBeAddedToStock) {
        zeroNegativeCheck(quantityToBeAddedToStock, "restock");
        quantityAvailable = Math.addExact(quantityAvailable, quantityToBeAddedToStock);
    }

    public void reduceStock(int quantityToBeTakenFromStock) {
        zeroNegativeCheck(quantityToBeTakenFromStock, "reduce stock");
        if (quantityToBeTakenFromStock > quantityAvailable) {
            throw new IllegalArgumentException("Not Enough Stock Available to fulfill this purchase");
        }
        quantityAvailable -= quantityToBeTakenFromStock;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }
}
