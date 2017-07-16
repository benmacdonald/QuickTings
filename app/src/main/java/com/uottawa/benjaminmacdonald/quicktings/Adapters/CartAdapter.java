package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 09/07/17.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>
        implements ShoppingCart.CompletionCallable {

    private class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        private CartAdapter mAdapter;
        private RecyclerView mRecyclerView;

        public ItemTouchHelperCallback(CartAdapter mAdapter, RecyclerView mRecyclerView) {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.mAdapter = mAdapter;
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            mAdapter.onItemRemove(viewHolder, mRecyclerView);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartImage;
        TextView cartItemName;
        TextView cartItemType;
        TextView cartItemAmount;
        TextView cartAmountCart;

        public ViewHolder(View parent) {
            super(parent);
            cartImage = (ImageView) parent.findViewById(R.id.cart_image);
            cartItemName = (TextView) parent.findViewById(R.id.cart_item_name);
            cartItemType = (TextView) parent.findViewById(R.id.cart_item_type);
            cartItemAmount = (TextView) parent.findViewById(R.id.cart_item_amount);
            cartAmountCart = (TextView) parent.findViewById(R.id.cart_amount_cart);
        }
    }

    public interface ReactableInterface {
        void reactTo(int position, CartItem item);
    }

    private ReactableInterface mReactableInterface;
    private List<CartItem> mDataset;
    private Context mContext;
    private ShoppingCart shoppingCart;
    private RequestQueue mRequestQueue;
    private RecyclerView mRecyclerView;
    private List<CartItem> mItemsToRemove;

    public CartAdapter(Context mContext, List<CartItem> mDataset, @Nullable ReactableInterface mReactableInterface) {
        this.mReactableInterface = mReactableInterface;
        this.mDataset = mDataset;
        this.mContext = mContext;
        shoppingCart = ShoppingCart.getInstance(this);
        mRequestQueue = Volley.newRequestQueue(mContext);
        mItemsToRemove = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(this, recyclerView));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CartItem item = mDataset.get(position);
        Glide.with(mContext).load(item.image_url).into(holder.cartImage);
        holder.cartItemName.setText(item.item_name);
        holder.cartItemType.setText(item.item_type);
        holder.cartItemAmount.setText(NumberFormat.getCurrencyInstance().format(item.item_cost));
        holder.cartAmountCart.setText(item.quantity);
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
                                    .putExtra(ProductActivity.FROM_CART, true)
                                    .putExtra(ProductActivity.CART_QUANTITY, cartItem.mQuantity));

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

    public void onItemRemove(RecyclerView.ViewHolder mViewHolder, RecyclerView recyclerView) {
        int adapterPosition = mViewHolder.getAdapterPosition();
        mReactableInterface.reactTo(adapterPosition, mDataset.get(adapterPosition));
        mItemsToRemove.add(mDataset.remove(adapterPosition));
        notifyItemRemoved(adapterPosition);
    }

    @Override
    public void onComplete(CartItem item, int resultCode) {
        if (resultCode == ADDED) {
            int newPosition = mDataset.size();
            mDataset.add(newPosition, item);
        } else if (resultCode == REMOVED) {
            mDataset.remove(item);
        }
    }

    @Override
    public void onFinalize() {
        notifyDataSetChanged();
    }

    public void onPositiveResult(int position, CartItem item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
        mItemsToRemove.remove(item);
    }

    public void onNegativeResult() {
        for (CartItem item : mItemsToRemove) {
            shoppingCart.removeItem(item);
        }
    }
}