package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Activities.ProductActivity;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by thomas on 16/07/17.
 */

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView checkoutImage;
        TextView checkoutItemName;
        TextView checkoutItemType;
        TextView checkoutItemAmount;
        TextView checkoutItemCart;

        public ViewHolder(View parent) {
            super(parent);
            checkoutImage = (ImageView) parent.findViewById(R.id.checkout_image);
            checkoutItemName = (TextView) parent.findViewById(R.id.checkout_item_name);
            checkoutItemType = (TextView) parent.findViewById(R.id.checkout_item_type);
            checkoutItemAmount = (TextView) parent.findViewById(R.id.checkout_item_amount);
            checkoutItemCart = (TextView) parent.findViewById(R.id.checkout_amount_cart);
        }
    }

    private List<CartItem> mDataset;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public CheckoutAdapter(Context mContext, List<CartItem> mDataset) {
        this.mRequestQueue = Volley.newRequestQueue(mContext);
        this.mContext = mContext;
        this.mDataset = mDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CartItem item = mDataset.get(position);
        Glide.with(mContext).load(item.image_url).into(holder.checkoutImage);
        holder.checkoutItemName.setText(item.item_name);
        holder.checkoutItemType.setText(item.item_type);
        holder.checkoutItemAmount.setText(NumberFormat.getCurrencyInstance().format(item.item_cost));
        holder.checkoutItemCart.setText(item.quantity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CartItem cartItem = mDataset.get(holder.getAdapterPosition());
                String url = "https://lcboapi.com/products/" + cartItem.getKey() + "/?access_key=" + mContext.getString(R.string.api_key);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("result");
                            ProductItem item = new ProductItem(result);
                            mContext.startActivity(new Intent(mContext, ProductActivity.class)
                                    .putExtra(ProductActivity.INTENT_HASH, item)
                                    .putExtra(ProductActivity.FROM_CHECKOUT, true));

                        } catch (Exception e) {
                            //some error
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYAPP","ERROR" + error);
                    }
                });
                mRequestQueue.add(jsonObjectRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
