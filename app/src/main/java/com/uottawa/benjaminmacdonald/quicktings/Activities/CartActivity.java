package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.uottawa.benjaminmacdonald.quicktings.Adapters.CartAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //set up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartList = (ListView) findViewById(R.id.cart_list);

        CartAdapter adapter = new CartAdapter(this, new ArrayList<CartItem>());

        cartList.setAdapter(adapter);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void checkoutItems(View view) {
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}
