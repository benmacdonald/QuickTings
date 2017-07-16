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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by thomas on 09/07/17.
 */
public class ShoppingCart {

    public interface CompletionCallable {
        int ADDED = 0;
        int REMOVED = 1;
        int UPDATED = 2;
        void onComplete(CartItem item, int resultCode);
        void onFinalize();
    }

    @IgnoreExtraProperties
    public static class CartItem {

        //non-db related items
        @Exclude
        private Integer mId;
        @Exclude
        public Integer mQuantity;

        //db stored items
        public String item_name;
        public String item_type;
        public String quantity;
        public String image_url;
        public Double item_cost;

        public CartItem(CartItem item) {
            this.mId = item.mId;
            this.mQuantity = 1;
            this.quantity = "1";
            this.item_name = item.item_name;
            this.item_type = item.item_type;
            this.item_cost = item.item_cost;
            this.image_url = item.image_url;
        }

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
        public String getKey() {
            return String.valueOf(mId);
        }

        @Exclude
        private void convertQuantity() {
            quantity = String.valueOf(mQuantity);
        }

        @Exclude
        private void convertAfterFetch(String key) {
            mQuantity = Integer.decode(quantity);
            mId = Integer.decode(key);
        }

        @Exclude
        private Double findTotalItemCost() {
            return mQuantity * item_cost;
        }

        @Exclude
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

    private final String SHOPPING_KEY = "shopping_cart";

    private double totalBill = 0.0;

    private static ShoppingCart INSTANCE;
    private HashSet<CartItem> cartItems;

    private final FirebaseDatabase database;
    private List<CompletionCallable> completionCallables = new ArrayList<>();

    private ValueEventListener fetchData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot dbData : dataSnapshot.getChildren()) {
                CartItem item = dbData.getValue(CartItem.class);
                item.convertAfterFetch(dbData.getKey());
                cartItems.add(item);
                totalBill += item.findTotalItemCost();
                Log.w("loadCart:loadedItem", "Added the item with the id: " + item.getKey());
                callAllCompleteListeners(item, CompletionCallable.ADDED);
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

        database.getReference()
                .child(SHOPPING_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(fetchData);
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
        //check to see if the item exists in the cart already
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem sItem = iterator.next();
            if (item.equals(sItem)) {
                sItem.setQuantity(sItem.mQuantity + item.mQuantity);
                database.getReference()
                        .child(SHOPPING_KEY)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(sItem.getKey())
                        .child("quantity")
                        .setValue(sItem.quantity);
                totalBill += item.findTotalItemCost();
                callAllCompleteListeners(sItem, CompletionCallable.UPDATED);
                callAllFinalizeListeners();
                return;
            }
        }

        database.getReference()
                .child(SHOPPING_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(item.getKey())
                .setValue(item);
        cartItems.add(item);
        totalBill += item.findTotalItemCost();
        callAllCompleteListeners(item, CompletionCallable.ADDED);
        callAllFinalizeListeners();
    }

    public void removeItem(CartItem item) {
        //remove from item if it exists
        database.getReference()
                .child(SHOPPING_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(item.getKey())
                .removeValue();
        cartItems.remove(item);
        totalBill -= item.findTotalItemCost();
        callAllCompleteListeners(item, CompletionCallable.REMOVED);
        callAllFinalizeListeners();
    }

    public void setItemQuantity(CartItem item) {
        //set the quantity in the child if it exists
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem sItem = iterator.next();
            if (item.equals(sItem)) {
                //find the difference and add it to the bill
                int diff = item.mQuantity - sItem.mQuantity;
                totalBill += diff * item.item_cost;
                sItem.setQuantity(item.mQuantity);
                database.getReference()
                        .child(SHOPPING_KEY)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(item.getKey())
                        .child("quantity")
                        .setValue(sItem.quantity);
                callAllCompleteListeners(sItem, CompletionCallable.UPDATED);
                callAllFinalizeListeners();
                break;
            }
        }
    }

    public Set<CartItem> getCart() {
        return cartItems;
    }

    public String getTotalBill() {
        return NumberFormat.getCurrencyInstance().format(totalBill);
    }

    public void clearCart() {
        totalBill = 0.0;
        cartItems.clear();
        completionCallables.clear();

        //repopulate the cart with new user data
        database.getReference()
                .child(SHOPPING_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(fetchData);
    }

    public void addListener(CompletionCallable completionCallable) {
        completionCallables.add(completionCallable);
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
