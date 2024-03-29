package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

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

        TextView alcoholPercentLabel = (TextView) rootView.findViewById(R.id.alcoholPercentLabel);
        alcoholPercentLabel.setTypeface(EasyFonts.robotoMedium(getActivity()));
        TextView alcoholPercent = (TextView) rootView.findViewById(R.id.alcoholPercentText);
        alcoholPercent.setText(""+productItem.getAlcoholContent()/100+"%");
        alcoholPercent.setTypeface(EasyFonts.robotoRegular(getActivity()));

        TextView packageTypeLabel = (TextView) rootView.findViewById(R.id.packageTypeLabel);
        packageTypeLabel.setTypeface(EasyFonts.robotoMedium(getActivity()));
        TextView packageType = (TextView) rootView.findViewById(R.id.packageTypeText);
        packageType.setText(productItem.getUnitType().substring(0, 1).toUpperCase() + productItem.getUnitType().substring(1));
        packageType.setTypeface(EasyFonts.robotoRegular(getActivity()));

        TextView makeLabel = (TextView) rootView.findViewById(R.id.madeLabel);
        makeLabel.setTypeface(EasyFonts.robotoMedium(getActivity()));
        TextView make = (TextView) rootView.findViewById(R.id.madeText);
        String country = productItem.getOrigin().split(",")[0];
        make.setText(country);
        make.setTypeface(EasyFonts.robotoRegular(getActivity()));

        TextView producerLabel = (TextView) rootView.findViewById(R.id.producerLabel);
        producerLabel.setTypeface(EasyFonts.robotoMedium(getActivity()));
        TextView producer = (TextView) rootView.findViewById(R.id.producerText);
        producer.setText(productItem.getProducerName());

        TextView styleLabel = (TextView) rootView.findViewById(R.id.styleLabel);
        styleLabel.setTypeface(EasyFonts.robotoMedium(getActivity()));
        TextView style = (TextView) rootView.findViewById(R.id.styleText);
        style.setText(productItem.getStyle());
        style.setTypeface(EasyFonts.robotoRegular(getActivity()));

        return rootView;
    }
}
