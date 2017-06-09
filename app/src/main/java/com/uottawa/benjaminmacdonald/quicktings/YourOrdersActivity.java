package com.uottawa.benjaminmacdonald.quicktings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class YourOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
