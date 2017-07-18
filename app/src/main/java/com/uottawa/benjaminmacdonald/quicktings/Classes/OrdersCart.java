package com.uottawa.benjaminmacdonald.quicktings.Classes;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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

        public String order_date;
        public Double longitude;
        public Double latitude;
        public Double order_cost;
        public Map<String, CartItem> order_items;

        public Order() {}

        @Exclude
        private void makeMap() {
            Set<CartItem> items = ShoppingCart.getInstance().getCart();
            if (order_items == null) {
                order_items = new HashMap<>();
            }
            for (CartItem item : items) {
                order_items.put(item.getKey(), item);
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
    }

    private static OrdersCart INSTANCE;
    private static final String ORDERS_KEY = "orders";
    private static final String RECENT_ORDER_KEY = "recent_order";

    private CompletionCallable mCompletionCallable;
    private FirebaseDatabase mDatabase;
    private Order mCurrentOrder;
    private Order mRecentOrder;

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
