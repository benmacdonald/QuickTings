package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Adapters.CartAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.OrdersCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.CompletionCallable;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity
        implements CompletionCallable, CartAdapter.ReactableInterface {

    private CartAdapter adapter;
    private TextView cartAmount;
    private ShoppingCart shoppingCart;
    private View undoBar;
    private Button undoButton;

    private int recentPosition;
    private ProgressDialog progressDialog;

    private Runnable refreshUndo = new Runnable() {
        @Override
        public void run() {
            shoppingCart.softCommit();
            undoBar.setVisibility(View.GONE);
            recentPosition = -1;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shoppingCart = ShoppingCart.getInstance(this);
        cartAmount = (TextView) findViewById(R.id.cart_amount);
        undoButton = (Button) findViewById(R.id.undoBtn);
        undoBar = findViewById(R.id.undoBar);
        //progressBar = (ProgressBar) findViewById(R.id.cart_progress_bar);
        adapter = new CartAdapter(this, new ArrayList<>(shoppingCart.getCart()), this);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem recentItem = shoppingCart.undoRecent();
                adapter.onPositiveResult(recentPosition, recentItem);
                undoBar.setVisibility(View.GONE);
            }
        });

        RecyclerView cartList = (RecyclerView) findViewById(R.id.cart_list);
        cartList.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void reactTo(int position, CartItem item) {
        recentPosition = position;
        undoBar.removeCallbacks(refreshUndo);
        undoBar.setVisibility(View.VISIBLE);
        undoBar.postDelayed(refreshUndo, 5000);
    }

    @Override
    public void beginReaction() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Finding Product");
        }
        progressDialog.show();
    }

    @Override
    public void endReaction() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void checkoutItems(View view) {
        OrdersCart.getInstance().newOrder();
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
}
