package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.uottawa.benjaminmacdonald.quicktings.R;

/**
 * Created by BenjaminMacDonald on 2017-07-12.
 */

public class PaymentFragment extends Fragment implements Step {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PaymentFragment() {}

    public static PaymentFragment newInstance(int sectionNumber) {
        PaymentFragment fragment = new PaymentFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_payment, container, false);


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
