package com.uottawa.benjaminmacdonald.quicktings.Classes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.CompletionCallable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by thomas on 17/07/17.
 */

public class OrdersCart implements ValueEventListener {

    @IgnoreExtraProperties
    public class Order {

        @Exclude
        public String order_key;

        public String order_date;
        public Double longitude;
        public Double latitude;
        public Double order_cost;
        public Map<String, Object> order_items;

        public Order() {}

        @Exclude
        private void makeMap() {
            Set<CartItem> items = ShoppingCart.getInstance().getCart();
            if (order_items == null) {
                order_items = new HashMap<>();
            }
            for (CartItem item : items) {
                order_items.put(item.getKey(), item.map());
            }
            order_cost = ShoppingCart.getInstance().getBill();
        }

        public void setLocation(LatLng coordinates) {
            latitude = coordinates.latitude;
            longitude = coordinates.longitude;
        }

        public boolean hasLocation() {
            return longitude != null && latitude != null;
        }

        public boolean hasAllData() {
            return order_date != null && longitude != null
                    && latitude != null && order_cost != null
                    && order_items != null && !order_items.isEmpty();
        }
    }

    private static OrdersCart INSTANCE;
    private static final String ORDERS_KEY = "orders";
    private static final String RECENT_ORDER_KEY = "recent_order";
    private static final String ALL_ORDER_KEY = "all_orders";

    private CompletionCallable mCompletionCallable;
    private FirebaseDatabase mDatabase;
    private Order mCurrentOrder;
    private String mRecentOrder;

    private ValueEventListener getRecentKey = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public static OrdersCart getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrdersCart();
        }
        return INSTANCE;
    }

    private OrdersCart() {
        mDatabase = FirebaseDatabase.getInstance();
        newOrder();
    }

    public Order newOrder() {
        mCurrentOrder = new Order();
        mCurrentOrder.makeMap();
        return mCurrentOrder;
    }

    public Order getCurrentOrder() {
        return mCurrentOrder;
    }

    public void addCurrentOrder() {
        //go to the all order db ref
        mCurrentOrder.order_key = mDatabase.getReference()
                .child(ORDERS_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(ALL_ORDER_KEY)
                .push()
                .getKey();

        //add the value
        mDatabase.getReference()
                .child(ORDERS_KEY)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(ALL_ORDER_KEY)
                .child(mCurrentOrder.order_key)
                .setValue(mCurrentOrder)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mCompletionCallable.onFinalize();
                    }
                });
        mRecentOrder = mCurrentOrder.order_key;
        mCurrentOrder = null;
    }

    public void addListener(CompletionCallable completionCallable) {
        mCompletionCallable = completionCallable;
    }

    public void removeListener() {
        mCompletionCallable = null;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
