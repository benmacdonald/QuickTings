package com.uottawa.benjaminmacdonald.quicktings.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by thomas on 09/07/17.
 */

public class CartAdapter extends ArrayAdapter<CartItem> implements ShoppingCart.CompletionCallable {

    //cache item
    private static class ViewHolder {
        ImageView cartImage;
        TextView cartItemName;
        TextView cartItemType;
        TextView cartItemAmount;
        TextView cartAmountCart;
    }

    private ShoppingCart shoppingCart;

    public CartAdapter(Context context, List<CartItem> items) {
        super(context, R.layout.card_cart, items);
        shoppingCart = ShoppingCart.getInstance(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CartItem item = getItem(position);
        ViewHolder cachedItem;

        if (convertView == null) {
            //create the cache
            cachedItem = new ViewHolder();
            //create the view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.card_cart, parent, false);
            convertView.findViewById(R.id.cart_remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new AlertDialog.Builder(getContext()))
                            .setTitle("Remove the item")
                            .setMessage("Are you sure you want remove the item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shoppingCart.removeItem(item);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
            cachedItem.cartImage = (ImageView) convertView.findViewById(R.id.cart_image);
            cachedItem.cartItemName = (TextView) convertView.findViewById(R.id.cart_item_name);
            cachedItem.cartItemType = (TextView) convertView.findViewById(R.id.cart_item_type);
            cachedItem.cartItemAmount = (TextView) convertView.findViewById(R.id.cart_item_amount);
            cachedItem.cartAmountCart = (TextView) convertView.findViewById(R.id.cart_amount_cart);
            convertView.setTag(cachedItem);
        } else {
            cachedItem = (ViewHolder) convertView.getTag();
        }

        Glide.with(getContext()).load(item.image_url).into(cachedItem.cartImage);
        cachedItem.cartItemName.setText(item.item_name);
        cachedItem.cartItemType.setText(item.item_type);
        cachedItem.cartItemAmount.setText(NumberFormat.getCurrencyInstance().format(item.item_cost));
        cachedItem.cartAmountCart.setText(item.quantity);
        return convertView;
    }

    @Override
    public void onComplete(CartItem item, int resultCode) {
        if (resultCode == GET) {
            add(item);
        } else if (resultCode == REMOVED) {
            remove(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onFinalize() {}
}
