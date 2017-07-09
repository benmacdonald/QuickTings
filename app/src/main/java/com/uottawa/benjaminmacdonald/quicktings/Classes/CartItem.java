package com.uottawa.benjaminmacdonald.quicktings.Classes;

/**
 * Created by thomas on 09/07/17.
 */

public class CartItem {

    private Integer itemId;
    private Integer quantity;

    public CartItem(Integer itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public void incQuantity() {
        quantity++;
    }

    public void decQuantity() {
        quantity--;
    }

    public Integer getItem() {
        return itemId;
    }
}
