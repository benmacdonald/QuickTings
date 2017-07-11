package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
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

    public static final String HASH_KEY = "hash_set_key";

    private CartAdapter adapter;
    private TextView cartAmount;
    private ShoppingCart shoppingCart = ShoppingCart.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        shoppingCart = ShoppingCart.getInstance(this);

        //set up back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<ShoppingCart.CartItem> values = new ArrayList<>();
        if (savedInstanceState != null) {
            values.addAll((HashSet<ShoppingCart.CartItem>) savedInstanceState.getSerializable(HASH_KEY));
        } else if (!shoppingCart.getCart().isEmpty()) {
            values.addAll(shoppingCart.getCart());
        }
        adapter = new CartAdapter(this, new ArrayList<>(values));
        ListView cartList = (ListView) findViewById(R.id.cart_list);
        cartList.setAdapter(adapter);

        cartAmount = (TextView) findViewById(R.id.cart_amount);
    }

    @Override
    public void finish() {
        super.finish();
        shoppingCart.removeListener(this);
        shoppingCart.removeListener(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putSerializable(HASH_KEY, shoppingCart.getCart());
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
        startActivity(new Intent(this, CheckoutActivity.class));
    }
}
