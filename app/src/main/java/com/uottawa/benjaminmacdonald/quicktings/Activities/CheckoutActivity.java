package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uottawa.benjaminmacdonald.quicktings.Fragments.CheckoutFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DeliveryFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.MainFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.PaymentFragment;
import com.uottawa.benjaminmacdonald.quicktings.R;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            CheckoutFragment checkoutFragment = new CheckoutFragment();
            // Intent, pass the Intent's extras to the fragment as arguments
            checkoutFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, checkoutFragment, "CHECKOUT_FRAG");
            transaction.addToBackStack("main");
            transaction.commit();
        }

        Button checkout = (Button) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutFragment checkoutFragment = new CheckoutFragment();
                // Intent, pass the Intent's extras to the fragment as arguments
                checkoutFragment.setArguments(getIntent().getExtras());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, checkoutFragment, "CHECKOUT_FRAG");
                transaction.addToBackStack("main");
                transaction.commit();
            }
        });

        Button delivery = (Button) findViewById(R.id.delivery);
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryFragment deliveryFragment = new DeliveryFragment();
                // Intent, pass the Intent's extras to the fragment as arguments
                deliveryFragment.setArguments(getIntent().getExtras());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, deliveryFragment, "DELIVERY_FRAG");
                transaction.addToBackStack("main");
                transaction.commit();
            }
        });

        Button payment = (Button) findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFragment paymentFragment = new PaymentFragment();
                // Intent, pass the Intent's extras to the fragment as arguments
                paymentFragment.setArguments(getIntent().getExtras());
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, paymentFragment, "PAYMENT_FRAG");
                transaction.addToBackStack("main");
                transaction.commit();
            }
        });


    }
}
