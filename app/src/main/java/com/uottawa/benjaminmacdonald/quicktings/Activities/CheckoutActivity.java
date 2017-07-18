package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.uottawa.benjaminmacdonald.quicktings.Classes.OrdersCart;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.CheckoutFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DeliveryFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.PaymentFragment;
import com.uottawa.benjaminmacdonald.quicktings.R;

public class CheckoutActivity extends AppCompatActivity {

    // fragments
    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // set up section
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), this));


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int currentStep = mStepperLayout.getCurrentStepPosition();
                if (currentStep == 1) {
                    if (!OrdersCart.getInstance().getCurrentOrder().hasLocation()) {
                        Toast.makeText(CheckoutActivity.this, "Please find your location", Toast.LENGTH_LONG).show();
                    } else {
                        mStepperLayout.proceed();
                    }
                } else if (currentStep == 2) {

                    startActivity(new Intent(CheckoutActivity.this, ConfirmationActivity.class));
                } else {
                    mStepperLayout.proceed();
                }
            }
        });

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //Back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        if (mStepperLayout.getCurrentStepPosition() > 0) {
            mStepperLayout.onBackClicked();
        } else {
            finish();
        }
        return true;
    }

    //Override back button
    @Override
    public void onBackPressed() {
        if (mStepperLayout.getCurrentStepPosition() > 0) {
            mStepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    // inner class
    public class SectionsPagerAdapter extends AbstractFragmentStepAdapter {

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {

            if (position == 0) {
                CheckoutFragment step = new CheckoutFragment();
                return step;
            }
            else if (position == 1) {
                DeliveryFragment step = new DeliveryFragment();
                return step;
            }
            PaymentFragment step = new PaymentFragment();

            return step;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            //Override this method to set Step title for the Tabs, not necessary for other stepper types
            if (position == 0) {
                setTitle("Checkout");
                return new StepViewModel.Builder(context)
//                        .setTitle("Checkout")
                        .create();
            }
            else if (position == 1) {
                setTitle("Delivery");
                return new StepViewModel.Builder(context)
//                        .setTitle("Delivery") //can be a CharSequence instead
                        .create();
            }
            setTitle("Payment");
            return new StepViewModel.Builder(context)
//                    .setTitle("Payment") //can be a CharSequence instead
                    .create();
        }

    }
}
