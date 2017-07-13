package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Classes.DatabaseUtils;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.DatabaseCallback;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-06-10.
 */

public class ProductItemArrayAdapter extends ArrayAdapter<ProductItem> implements DatabaseCallback {

    private final Context context;
    private final List<ProductItem> values;
    private DatabaseUtils databaseUtils;


    public ProductItemArrayAdapter(Context context, List<ProductItem> values) {
        super(context, R.layout.card_search_result, values);
        this.context = context;
        this.values = values;
        this.databaseUtils = new DatabaseUtils(this);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_search_result, parent, false);

        //Set the title of the product
        TextView productTitle = (TextView) listView.findViewById(R.id.productTitle);
        productTitle.setText(values.get(position).getName());
        productTitle.setTypeface(EasyFonts.robotoBlack(context));

        //Set the type
        TextView productType = (TextView) listView.findViewById(R.id.productType);
        productType.setText(values.get(position).getCategory());
        productType.setTypeface(EasyFonts.robotoRegular(context));

        //Set price
        TextView productPrice = (TextView) listView.findViewById(R.id.productPrice);
        productPrice.setText(formatter.format(values.get(position).getPrice() / 100.00));
        productPrice.setTypeface(EasyFonts.robotoBold(context));

        //get volume
        TextView productVolume = (TextView) listView.findViewById(R.id.productVolume);
        productVolume.setText(values.get(position).getPackageType());
        productVolume.setTypeface(EasyFonts.robotoRegular(context));

        //get image
        ImageView imageView = (ImageView) listView.findViewById(R.id.productImage);
        Glide.with(context).load(values.get(position).getImageUrl()).into(imageView);

        //favourite button
        final ImageButton favouriteButton = (ImageButton) listView.findViewById(R.id.favouriteButton);

        if (databaseUtils.getFavouritesHashMap().containsKey(String.valueOf(values.get(position).getId()))) {
            favouriteButton.setColorFilter(Color.parseColor("#D64747"));
        } else {
            favouriteButton.setColorFilter(Color.parseColor("#AAA9A9"));
        }

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (databaseUtils.getFavouritesHashMap().containsKey(String.valueOf(values.get(position).getId()))) {
                    favouriteButton.setColorFilter(context.getResources().getColor(R.color.colorInactive));
                    databaseUtils.removeFromFavourite(values.get(position).getId());
                } else {
                    favouriteButton.setColorFilter(context.getResources().getColor(R.color.colorFavourite));
                    databaseUtils.addToFavourite(values.get(position).getId());
                }

            }
        });

        return listView;
    }

    @Override
    public int getCount() {
        return (this.values != null) ? this.values.size() : 0;
    }

    public ProductItem getItem(int position) {
        return values.get(position);
    }

    @Override
    public void callback(HashMap o) {

    }
}
