package com.uottawa.benjaminmacdonald.quicktings.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.uottawa.benjaminmacdonald.quicktings.Classes.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.List;

/**
 * Created by thomas on 09/07/17.
 */

public class CartAdapter extends ArrayAdapter<CartItem> {

    public CartAdapter(Context context, List<CartItem> items) {
        super(context, R.layout.card_cart, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_cart,parent,false);

        return listView;
    }
}
