package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by thomas on 06/07/17.
 */

public class ShoppingCart {

    private static ShoppingCart instance = null;
    private final FirebaseDatabase databaseReference;
    private FirebaseUser currentUser;

    private ShoppingCart() {
        databaseReference = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void setUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void addItem(String itemId, Integer quantity) {}

    public void removeItem(String itemId) {}

    public void clearCart() {}
}
