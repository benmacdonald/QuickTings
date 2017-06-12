package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-06-10.
 */

public class ProductItemArrayAdapter extends ArrayAdapter<ProductItem> {

    private final Context context;
    private final List<ProductItem> values;


    public ProductItemArrayAdapter(Context context, List<ProductItem> values) {
        super(context, R.layout.card_search_result, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_search_result,parent,false);

        //Set the title of the product
        TextView productTitle = (TextView) listView.findViewById(R.id.productTitle);
        productTitle.setText(values.get(position).getName());

        //Set the type
        TextView productType = (TextView) listView.findViewById(R.id.productType);
        productType.setText(values.get(position).getUnitType());

        //Set price
        TextView productPrice = (TextView) listView.findViewById(R.id.productPrice);
        productPrice.setText("$"+values.get(position).getPrice());

        //get volume
        TextView productVolume = (TextView) listView.findViewById(R.id.productVolume);
        productVolume.setText(values.get(position).getVolume() +" mL bottle");


        return listView;
    }

    @Override
    public int getCount() {
        return (this.values != null) ? this.values.size() : 0;
    }
}
