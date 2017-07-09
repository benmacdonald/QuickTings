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
 * Created by TommyNguyen on 2017-06-21.
 */

public class MainActivityArrayAdapter extends ArrayAdapter<ProductItem> {

    private final Context context;
    private final List<ProductItem> values;

    public MainActivityArrayAdapter(Context context, List<ProductItem> values) {
        super(context, R.layout.card_product, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_product,parent,false);

        //Set the title of the product
        TextView productTitle = (TextView) listView.findViewById(R.id.productTitle);
        productTitle.setText(values.get(position).getName());

        //Set the type
        TextView productType = (TextView) listView.findViewById(R.id.productType);
        productType.setText(values.get(position).getCategory());

        //Set price
        TextView productPrice = (TextView) listView.findViewById(R.id.productPrice);
        productPrice.setText("$"+values.get(position).getPrice()/100.00);

        //get image
        ImageView imageView = (ImageView) listView.findViewById(R.id.productImage);
        Glide.with(context).load(values.get(position).getImageUrl()).into(imageView);

        return listView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }
}
