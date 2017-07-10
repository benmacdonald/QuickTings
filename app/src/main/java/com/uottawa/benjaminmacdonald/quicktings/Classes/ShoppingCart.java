package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by thomas on 09/07/17.
 */

public class ShoppingCart implements Serializable {

    private static ShoppingCart INSTANCE;
    private HashSet<CartItem> cartItems;

    private transient final FirebaseDatabase database;
    private transient FirebaseUser user;

    private ShoppingCart() {
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        cartItems = new HashSet<>();
    }

    public static ShoppingCart getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingCart();
        }
        return INSTANCE;
    }

    public void addItem(CartItem item) {
        //first step add to cart struct
        cartItems.add(item);

        //add the item to the db cart
        DatabaseReference cartRef = database.getReference("/shopping-cart/" + user.getUid());
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public void removeItem(Integer itemId) {
        removeItem(new CartItem(itemId));
    }
}
