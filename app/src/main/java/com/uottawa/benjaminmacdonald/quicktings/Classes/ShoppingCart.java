package com.uottawa.benjaminmacdonald.quicktings.Classes;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by thomas on 09/07/17.
 */
public class ShoppingCart {

    public interface CompletionCallable {
        int ADDED = 0;
        int REMOVED = 1;
        int GET = 2;
        void onComplete(CartItem item, int resultCode);
        void onFinalize();
    }

    @IgnoreExtraProperties
    public static class CartItem {

        //non-db related items
        @Exclude
        public Integer mId;
        @Exclude
        public Integer mQuantity;

        //db stored items
        public String item_name;
        public String item_type;
        public String quantity;
        public String image_url;
        public Double item_cost;

        public CartItem(String itemName, String itemType, String image_url, Integer mId, Integer itemCost, Integer quantity) {
            this.item_name = itemName;
            this.item_type = itemType;
            this.image_url= image_url;
            this.mId = mId;
            this.item_cost = itemCost / 100.00;
            if (quantity <= 1) {
                this.mQuantity = 1;
            } else {
                this.mQuantity = quantity;
            }
            convertQuantity();
        }

        public CartItem(String itemName, String itemType, String imageURL, Integer itemId, Integer itemCost) {
            this(itemName, itemType, imageURL, itemId, itemCost, 1);
        }

        public CartItem() {}

        @Exclude
        public void setQuantity(Integer mQuantity) {
            this.mQuantity = mQuantity;
            convertQuantity();
        }

        @Exclude
        public void incQuantity() {
            mQuantity++;
            convertQuantity();
        }

        @Exclude
        public void decQuantity() {
            if (mQuantity > 1) {
                mQuantity--;
            }
            convertQuantity();
        }

        @Exclude
        private String getKey() {
            return String.valueOf(mId);
        }

        @Exclude
        private void convertQuantity() {
            quantity = String.valueOf(mQuantity);
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

            return mId.equals(castedObject.mId);
        }
    }

    private double totalBill = 0.0;

    private static ShoppingCart INSTANCE;
    private HashSet<CartItem> cartItems;

    private final FirebaseDatabase database;
    private List<CompletionCallable> completionCallables = new ArrayList<>();

    private ValueEventListener getCart = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot dbData : dataSnapshot.getChildren()) {
                CartItem item = dbData.getValue(CartItem.class);
                item.mQuantity = Integer.decode(item.quantity);
                String key = dbData.getKey();
                item.mId = Integer.decode(key);
                cartItems.add(item);
                totalBill += item.mQuantity * item.item_cost;
                Log.w("loadCart:loadedItem", "Added the item with the id: " + item.getKey());
                callAllCompleteListeners(item, CompletionCallable.GET);
            }
            Log.w("loadCart:onComplete", "finished loading the cart");
            callAllFinalizeListeners();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("loadCart:onCancelled", databaseError.toException());
        }
    };

    private ShoppingCart(CompletionCallable completionCallable) {
        if (completionCallable != null) {
            completionCallables.add(completionCallable);
        }

        database = FirebaseDatabase.getInstance();
        cartItems = new HashSet<>();

        DatabaseReference ref = database.getReference().child("shopping_cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(getCart);
    }

    public static ShoppingCart getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingCart(null);
        }
        return INSTANCE;
    }

    public static ShoppingCart getInstance(CompletionCallable completionCallable) {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingCart(completionCallable);
        } else {
            INSTANCE.completionCallables.add(completionCallable);
        }
        return INSTANCE;
    }

    public void addItem(CartItem item) {
        //first step add to cart struct
        cartItems.add(item);
        totalBill += item.mQuantity * item.item_cost;

        //add the item to the db cart
        DatabaseReference cartRef = database.getReference("shopping_cart/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.child(item.getKey()).setValue(item);
        callAllCompleteListeners(item, CompletionCallable.ADDED);
        callAllFinalizeListeners();
    }

    public void removeItem(CartItem item) {
        //add to data struct for local things
        cartItems.remove(item);
        totalBill -= item.mQuantity * item.item_cost;

        //remove the item from the cart
        DatabaseReference cartRef = database.getReference("shopping_cart/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        cartRef.child(item.getKey()).removeValue();
        callAllCompleteListeners(item, CompletionCallable.REMOVED);
        callAllFinalizeListeners();
    }

    public HashSet<CartItem> getCart() {
        return cartItems;
    }

    public String getTotalBill() {
        return NumberFormat.getCurrencyInstance().format(totalBill);
    }

    public void clearCart() {
        totalBill = 0.0;
        cartItems.clear();
        completionCallables.clear();
    }

    public void removeListener(CompletionCallable completionCallable) {
        completionCallables.remove(completionCallable);
    }

    private void callAllCompleteListeners(CartItem item, int resultCode) {
        //notify the caller of the change
        if (!completionCallables.isEmpty()) {
            for (CompletionCallable completionCallable : completionCallables) {
                completionCallable.onComplete(item, resultCode);
            }
        }
    }

    private void callAllFinalizeListeners() {
        if (!completionCallables.isEmpty()) {
            for (CompletionCallable completionCallable : completionCallables) {
                completionCallable.onFinalize();
            }
        }
    }
}
