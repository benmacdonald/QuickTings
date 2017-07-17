package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.CheckoutAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;

/**
 * Created by BenjaminMacDonald on 2017-07-12.
 */

public class CheckoutFragment extends Fragment implements Step {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public CheckoutFragment() {}

    public static CheckoutFragment newInstance(int sectionNumber) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        //shared preferences

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.checkout_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CheckoutAdapter(getContext(), new ArrayList<>(ShoppingCart.getInstance().getCart())));


        return rootView;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
