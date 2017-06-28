package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by TommyNguyen on 2017-06-28.
 */

public class DiscoverArrayAdapter extends ArrayAdapter<ProductItem> {

    private final Context context;
    private final List<ProductItem> discoverValues;

    public DiscoverArrayAdapter(Context context, List<ProductItem> values) {
        super(context, R.layout.card_product, values);
        this.context = context;
        this.discoverValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_discover,parent,false);

        //TODO: Need to decide what we want to do with these

        return listView;
    }

}
