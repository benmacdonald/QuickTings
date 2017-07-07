package com.uottawa.benjaminmacdonald.quicktings.Classes;

/**
 * Created by thomas on 07/07/17.
 */

public class CartItem {

    private Integer itemId;
    private Integer quantity;

    public CartItem(Integer itemId, Integer quantity) {
        this.itemId = itemId;

        if (quantity >= 1) {
            this.quantity = quantity;
        } else {
            this.quantity = 1;
        }
    }

    public CartItem(Integer itemId) {
        this(itemId, 1);
    }

    public void incQuantity() {
        quantity++;
    }

    public void decQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }
}
