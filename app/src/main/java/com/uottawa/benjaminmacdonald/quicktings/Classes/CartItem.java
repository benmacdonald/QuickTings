package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by thomas on 09/07/17.
 */
@IgnoreExtraProperties
public class CartItem {

    private String itemName;
    private String itemType;

    private Double itemCost;

    private Integer itemId;
    private Integer quantity;

    public CartItem(String itemName, String itemType, Integer itemId, Integer itemCost, Integer quantity) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemId = itemId;
        this.itemCost = itemCost / 100.00;
        if (quantity <= 1) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }

    public CartItem(String itemName, String itemType, Integer itemId, Integer itemCost) {
        this(itemName, itemType, itemId, itemCost, 1);
    }

    public CartItem(Integer itemId) {
        this.itemId = itemId;
    }

    @Exclude
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public void incQuantity() {
        quantity++;
    }

    @Exclude
    public void decQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }

    @Exclude
    public Integer getItem() {
        return itemId;
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() != object.getClass()) {
            return false;
        }

        CartItem castedObject = (CartItem) object;

        if (this == castedObject) {
            return true;
        }

        return itemId.equals(castedObject.itemId);
    }
}
