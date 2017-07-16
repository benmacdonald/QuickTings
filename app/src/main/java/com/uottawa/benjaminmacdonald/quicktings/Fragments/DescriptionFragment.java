package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;

/**
 * Created by BenjaminMacDonald on 2017-07-07.
 */

public class DescriptionFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PRODUCT_ITEM = "product_item";

    private ProductItem productItem;


    public DescriptionFragment() {}

    public static DescriptionFragment newInstance(int sectionNumber, ProductItem productItem) {
        DescriptionFragment fragment = new DescriptionFragment();
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
        final View rootView = inflater.inflate(R.layout.description_fragment, container, false);

        TextView productDescription = (TextView) rootView.findViewById(R.id.productDescription);
        FlexboxLayout flexbox = (FlexboxLayout) rootView.findViewById(R.id.flexbox);
        productDescription.setTypeface(EasyFonts.robotoMedium(getActivity()));
        if(! productItem.getDescription().equals("null")) {
            flexbox.setVisibility(View.GONE);
            productDescription.setText(productItem.getDescription());
        }
        else if (! productItem.getTastingNote().equals("null")) {
            flexbox.setVisibility(View.GONE);
            productDescription.setText(productItem.getTastingNote());
        }
        else {
            String[] tags = productItem.getTags().split(" ");
            ChipCloudConfig config = new ChipCloudConfig()
                    .selectMode(ChipCloud.SelectMode.none)
                    .checkedChipColor(Color.parseColor("#ddaa00"))
                    .checkedTextColor(Color.parseColor("#ffffff"))
                    .uncheckedChipColor(Color.parseColor("#efefef"))
                    .uncheckedTextColor(Color.parseColor("#666666"))
                    .useInsetPadding(true)
                    .typeface(EasyFonts.robotoMedium(getActivity()));
            ChipCloud chipCloud = new ChipCloud(getActivity(), flexbox, config);
            chipCloud.addChips(tags);
        }

        return rootView;
    }
}
