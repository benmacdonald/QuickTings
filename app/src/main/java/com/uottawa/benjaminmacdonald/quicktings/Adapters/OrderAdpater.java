package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.uottawa.benjaminmacdonald.quicktings.Classes.Orders;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Manifest;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-07-18.
 */

public class OrderAdpater extends ArrayAdapter<Orders> {

    private final Context context;
    private final List<Orders> values;
    private RequestQueue requestQueue;


    public OrderAdpater(Context context, List<Orders> values) {
        super(context, R.layout.card_order, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ShoppingCart shoppingCart = ShoppingCart.getInstance();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_order,parent,false);

        TextView total = (TextView) listView.findViewById(R.id.orderTotal);
        total.setText("Total Cost " + formatter.format(values.get(position).getOrder_cost()));

        TextView date = (TextView) listView.findViewById(R.id.orderDate);
        date.setText("Ordered on " + values.get(position).getOrder_date());

        ImageView map = (ImageView) listView.findViewById(R.id.mapImage);
        //https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&key=YOUR_API_KEY
        String lat = values.get(position).getLatitude().toString();
        String _long = values.get(position).getLongitude().toString();
        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+_long+"&zoom=15&size=200x200&scale=2&key=AIzaSyALZvqSgkzhLBtORw7iej52P3M-pvl4K5w";
        System.out.println(url);
        Glide.with(context).load(url).into(map);

        requestQueue = Volley.newRequestQueue(context);


        Button addAll = (Button) listView.findViewById(R.id.orderAgain);
        addAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                List<Integer> test = new ArrayList<Integer>();
                test.addAll(values.get(position).getProducts());
                for (Object productId : test) {
                    String url = "https://lcboapi.com/products/" + productId.toString() + "/?access_key=MDplN2Y5YTk1Yy0zNzJmLTExZTctYTBjZi1lZmFlNmMyYmFlY2U6ZDlTcWtYWEFhb0FUdU9vMFVPOFdTUXMwYWRUQUxkdnhLcG1v";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        System.out.println(response);
                                        JSONObject product = response.getJSONObject("result");
                                        ProductItem productItem = new ProductItem(product);
                                        //String itemName, String itemType, String image_url, Integer mId, Integer itemCost, Integer quantity\
                                        shoppingCart.addItem(new ShoppingCart.CartItem(
                                                productItem.getName(),
                                                productItem.getCategory(),
                                                productItem.getImageUrl(),
                                                productItem.getId(),
                                                productItem.getPrice(),
                                                1
                                        ));


                                    } catch (Exception e) {
                                        //swag
                                    }
                                }

                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("MYAPP","ERROR" + error);
                                }
                            });

                    requestQueue.add(jsonObjectRequest);
                }

                Toast.makeText(context, "Added "+test.size()+" items to cart", Toast.LENGTH_SHORT).show();
            }

        });


        return listView;
    }


    @Override
    public int getCount() {
        return this.values.size();
    }
}
