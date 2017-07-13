package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Adapters.CartAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CartActivity extends AppCompatActivity implements ShoppingCart.CompletionCallable {

    private CartAdapter adapter;
    private TextView cartAmount;
    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartAmount = (TextView) findViewById(R.id.cart_amount);
        ListView cartList = (ListView) findViewById(R.id.cart_list);

        shoppingCart = ShoppingCart.getInstance(this);
        adapter = new CartAdapter(this, new ArrayList<>(shoppingCart.getCart()));
        cartList.setAdapter(adapter);
        onFinalize();
    }

    @Override
    public void finish() {
        super.finish();
        shoppingCart.removeListener(this);
        shoppingCart.removeListener(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        shoppingCart.removeListener(this);
        shoppingCart.removeListener(adapter);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onComplete(ShoppingCart.CartItem item, int resultCode) {}

    @Override
    public void onFinalize() {
        cartAmount.setText(shoppingCart.getTotalBill());
    }

    public void checkoutItems(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
}
