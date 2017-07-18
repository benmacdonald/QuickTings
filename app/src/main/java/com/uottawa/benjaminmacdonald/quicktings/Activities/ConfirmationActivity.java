package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;


public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        ArrayList<ShoppingCart.CartItem> list = new ArrayList<>(ShoppingCart.getInstance().getCart());

        //initial setup
        Toolbar actionBar = (Toolbar) findViewById(R.id.confirmation_toolbar);
        LinearLayout orderList = (LinearLayout) findViewById(R.id.order_list);
        TextView totalOrder = (TextView) findViewById(R.id.order_total);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_confirmation));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < list.size(); i++) {
            ShoppingCart.CartItem item = list.get(i);
            inflater.inflate(R.layout.card_item_confirmation, orderList, true);
            View view = orderList.getChildAt(i);
            TextView t1 = (TextView) view.findViewById(R.id.order_item_name);
            TextView t2 = (TextView) view.findViewById(R.id.order_item_quantity);
            t1.setText(item.item_name);
            t2.setText(item.quantity);
        }

        totalOrder.setText(ShoppingCart.getInstance().getTotalBill());

        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingCart cart = ShoppingCart.getInstance();
                cart.clearCart();
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void trackOrder(View v) {

    }
}
