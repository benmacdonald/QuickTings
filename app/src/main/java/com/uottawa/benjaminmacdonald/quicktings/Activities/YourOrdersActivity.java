package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.OrderAdpater;
import com.uottawa.benjaminmacdonald.quicktings.Classes.Orders;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class YourOrdersActivity extends AppCompatActivity {

    private HashMap<String, HashMap<String,Object>> ordersHashMap;
    private ArrayList<Orders> orders;

    private OrderAdpater orderAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.orderList);

        ordersHashMap = new HashMap();

        orders = new ArrayList<>();

        orderAdpater = new OrderAdpater(this, orders);

        listView.setAdapter(orderAdpater);



        getOrders();

    }


    public void getOrders() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = database.getReference("orders/"+ user.getUid());

        // Attach a listener to read the data at our posts reference
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Orders> tmp = new ArrayList<Orders>();
                ordersHashMap = (HashMap<String, HashMap<String,Object>>) dataSnapshot.getValue();
                if (ordersHashMap == null) {
                    ordersHashMap = new HashMap<String, HashMap<String, Object>>();
                }
                for ( Map.Entry<String, HashMap<String,Object>> entry : ordersHashMap.entrySet()) {
                    String key = entry.getKey();
                    HashMap<String, Object> item = entry.getValue();
                    Orders order = new Orders();
                    if (item.get("order_cost") instanceof Double) {
                        order.setOrder_cost((Double) item.get("order_cost"));
                    } else if (item.get("order_cost") instanceof Long) {
                        order.setOrder_cost(Double.valueOf((Long) item.get("order_cost")));
                    }
                    order.setOrder_date((String) item.get("order_date"));
                    order.setLatitude((Double) item.get("latitude"));
                    order.setLongitude((Double) item.get("longitude"));
                    order.setProducts((List<Integer>) item.get("products"));

                    tmp.add(order);
                }
                orders.clear();
                orders.addAll(tmp);
                orderAdpater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    //Back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
