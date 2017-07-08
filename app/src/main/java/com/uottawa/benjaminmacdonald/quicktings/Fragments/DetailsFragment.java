package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

/**
 * Created by BenjaminMacDonald on 2017-07-07.
 */

public class DetailsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PRODUCT_ITEM = "product_item";
    private ProductItem productItem;


    public DetailsFragment() {}

    public static DetailsFragment newInstance(int sectionNumber, ProductItem productItem) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(PRODUCT_ITEM, productItem);
        fragment.setArguments(args);

        //shared preferences

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productItem = getArguments().getParcelable(PRODUCT_ITEM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.details_fragment, container, false);


        return rootView;
    }
}
