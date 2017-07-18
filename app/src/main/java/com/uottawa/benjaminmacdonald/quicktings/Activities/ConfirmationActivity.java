package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Classes.OrdersCart;
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

        OrdersCart ordersCart = OrdersCart.getInstance();

        String lat = ordersCart.getCurrentOrder().latitude.toString();
        String _long = ordersCart.getCurrentOrder().longitude.toString();

        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+_long+
                "&zoom=15&size=400x400&scale=2" +
                "&markers=color:red%7Clabel:C%7C+"+lat+","+_long+"&key=AIzaSyALZvqSgkzhLBtORw7iej52P3M-pvl4K5w";

        ImageView map = (ImageView) findViewById(R.id.confirmationMap);
        Glide.with(this).load(url).into(map);



        Button home = (Button) findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

        ShoppingCart.getInstance().clearCart();
        ordersCart.clearCurrentOrder();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void trackOrder(View v) {

    }
}
