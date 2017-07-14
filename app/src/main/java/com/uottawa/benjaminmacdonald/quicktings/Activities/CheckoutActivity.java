package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.CheckoutFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DeliveryFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DescriptionFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DetailsFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.MainFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.PaymentFragment;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    // fragments
    private CheckoutActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // set up section
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), this));

//        //set up tabs
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.checkoutTabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                .inflate(R.layout.custom_tab, null, false);
//
//        tabLayout.getTabAt(0).setCustomView(headerView);


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
                return new StepViewModel.Builder(context)
                        //.setTitle("Checkout")
                        .create();
            }
            else if (position == 1) {
                return new StepViewModel.Builder(context)
                        //.setTitle("Delivery") //can be a CharSequence instead
                        .create();
            }
            return new StepViewModel.Builder(context)
                    //.setTitle("Payment") //can be a CharSequence instead
                    .create();
        }

    }
}
