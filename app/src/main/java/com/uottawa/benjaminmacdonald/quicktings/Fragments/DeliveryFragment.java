package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uottawa.benjaminmacdonald.quicktings.R;

/**
 * Created by BenjaminMacDonald on 2017-07-12.
 */

public class DeliveryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public DeliveryFragment() {}

    public static DeliveryFragment newInstance(int sectionNumber) {
        DeliveryFragment fragment = new DeliveryFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_delivery, container, false);


        return rootView;
    }
}
