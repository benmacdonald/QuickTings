package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uottawa.benjaminmacdonald.quicktings.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
