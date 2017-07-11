package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart.CartItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.DatabaseUtils;
import com.uottawa.benjaminmacdonald.quicktings.Classes.Inventory;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.Store;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DescriptionFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.DetailsFragment;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.DatabaseCallback;
import com.uottawa.benjaminmacdonald.quicktings.Manifest;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements OnMapReadyCallback, DatabaseCallback {

    public static final String CART_ITEM_ADDED = "addedItem";

    private FABToolbarLayout toolbarLayout;

    private MapView mapView;
    private GoogleMap map;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private RequestQueue requestQueue;

    private ProductItem productItem;
    private DatabaseUtils databaseUtils;

    private CartItem cartItem;

    String description;
    String details;

    // fragments
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);
        addFont();

        //Set up database utils
        this.databaseUtils = new DatabaseUtils(this);

        // setup request queue
        requestQueue = Volley.newRequestQueue(this);

        //get product item
        Intent intent = getIntent();
        productItem = (ProductItem) intent.getExtras().getParcelable("PRODUCT");
        setupProduct(productItem);

        //set up tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.productPageTabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Adds onClickListener to FAB
        toolbarLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        View fab = findViewById(R.id.fabtoolbar_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLayout.show();
            }
        });

        //Closes fabtoolbar if user scrolls
        View scrollView = findViewById(R.id.contentView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                toolbarLayout.hide();
            }
        });

        //Cancel button
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                TextView quantityValue = (TextView) findViewById(R.id.quantityValue);
                quantityValue.setText("1");
                cartItem.setQuantity(1);
                calculateNewPrice(1);
                toolbarLayout.hide();
            }
        });

        //Add to Cart Button
        //TODO:: add to cart

        //Add Button
        Button addButton = (Button) findViewById(R.id.plusButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityValue = (TextView) findViewById(R.id.quantityValue);
                Integer value = Integer.parseInt(quantityValue.getText().toString());
                value = value + 1;
                quantityValue.setText(value.toString());
                cartItem.incQuantity();
                calculateNewPrice(value);
            }
        });

        //Subtract Button
        Button minusButton = (Button) findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityValue = (TextView) findViewById(R.id.quantityValue);
                Integer value = Integer.parseInt(quantityValue.getText().toString());
                value = value - 1;
                cartItem.decQuantity();
                if (value > 0) {
                    quantityValue.setText(value.toString());
                    calculateNewPrice(value);
                }

            }
        });

        Button addToCartButton = (Button) findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart.getInstance().addItem(cartItem);
                setResult(RESULT_OK, (new Intent()).putExtra(CART_ITEM_ADDED, true));
                finish();
            }
        });

        //Setting up mapview
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //get user current location
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                for (Location location : locationResult.getLocations()) {
//                    // Update UI with location data
//                    // ...
//                    System.out.println(location);
//                }
//            };
//        };


        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, null);


    }

    //
    public void exitWithData() {

        Intent returnIntent = new Intent();
        boolean isFavourite = databaseUtils.getFavouritesHashMap().containsKey(String.valueOf(productItem.getId()));
        returnIntent.putExtra("isFavourite", isFavourite);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        //Checks if the FAB is the button or the toolbar
        if (toolbarLayout.isFab()) {
            exitWithData();
        } else {
            toolbarLayout.hide();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);

        //check favourite button if favourited
        if (databaseUtils.getFavouritesHashMap().containsKey(String.valueOf(productItem.getId()))) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favourite_white_24dp));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_fav) {
            if (databaseUtils.getFavouritesHashMap().containsKey(String.valueOf(productItem.getId()))) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favourite_border_white_24dp));
                databaseUtils.removeFromFavourite(productItem.getId());
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favourite_white_24dp));
                databaseUtils.addToFavourite(productItem.getId());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //ActionBar back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        exitWithData();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MAPS_RECEIVE) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        updateAvalibilityandMap(location.getLatitude(), location.getLongitude());
//                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
//                        map.addMarker(new MarkerOptions().position(current).title("Current Location"));
//                        map.moveCamera(CameraUpdateFactory.newLatLng(current));
                        //lcboapi.com/stores?lat=43.659&lon=-79.439
                    } else {
                        TextView amount = (TextView) findViewById(R.id.numAvaliable);
                        amount.setText("Available");
                        defaultMap();
                    }
                }
            });
        }
//        LatLng sydney = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void callback(HashMap o) {

    }

    private void calculateNewPrice(Integer quantity) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        TextView price = (TextView) findViewById(R.id.priceLabel2);
        String newPrice = formatter.format(quantity * (productItem.getPrice() / 100.00));
        price.setText(newPrice);
    }

    private void setupProduct(ProductItem productItem) {

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        ImageView productImage = (ImageView) findViewById(R.id.productImage);
        Glide.with(this).load(productItem.getImageUrl()).into(productImage);

        TextView nameView = (TextView) findViewById(R.id.nameLabel);
        nameView.setText(productItem.getName());

        TextView nameView2 = (TextView) findViewById(R.id.nameLabel2);
        nameView2.setText(productItem.getName());

        TextView catView = (TextView) findViewById(R.id.catLabel);
        catView.setText(productItem.getCategory());

        TextView priceView = (TextView) findViewById(R.id.priceLabel);
        priceView.setText(formatter.format(productItem.getPrice() / 100.00));

        TextView priceView2 = (TextView) findViewById(R.id.priceLabel2);
        priceView2.setText(formatter.format(productItem.getPrice() / 100.00));

        TextView volumeView = (TextView) findViewById(R.id.volumeLabel);
        volumeView.setText(productItem.getVolume() + " mL " +productItem.getUnitType());

        description = productItem.getDescription();
        details = productItem.getOrigin();

        cartItem = new CartItem(productItem.getName(), productItem.getCategory(), productItem.getImageUrl(), productItem.getId(), productItem.getRegularPrice());

//        TextView textToChange = (TextView) findViewById(R.id.textToChange);
//        if (productItem.getDescription() == null) {
//            //TODO: If description returns null, do something
//        } else {
//            textToChange.setText(productItem.getDescription());
//        }

    }



    private void updateAvalibilityandMap(Double latitude, Double longitude) {
        String url = "https://lcboapi.com/stores?access_key="+getString(R.string.api_key)+"&lat="+latitude+"&lon="+longitude;
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Store> tmp = createStores(response);
                        Store store = tmp.get(0);
                        TextView storeName = (TextView) findViewById(R.id.storeName);
                        TextView storeAddPhone = (TextView) findViewById(R.id.storeAddPhone);
                        storeName.setText(store.getName() + " LCBO");
                        storeAddPhone.setText(store.getAddress_line_1() + " • " + store.getTelephone());
                        updateStockQuantitiy(store.getId());

                        LatLng closestStore = new LatLng(store.getLatitude(), store.getLongitude());
                        Marker marker = map.addMarker(new MarkerOptions().position(closestStore));
                        marker.showInfoWindow();
                        map.moveCamera(CameraUpdateFactory.newLatLng(closestStore));
                        map.animateCamera(CameraUpdateFactory.zoomTo(14));

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYAPP","ERROR" + error);

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void updateStockQuantitiy(int storeid) {
        //lcboapi.com/stores/{store_id}/products/{product_id}/inventory
        String url = "https://lcboapi.com/stores/"+storeid+"/products/"+productItem.getId()+"/inventory?access_key="+getString(R.string.api_key);
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Inventory currentInventory = new Inventory(createInventory(response));

                        TextView amount = (TextView) findViewById(R.id.numAvaliable);
                        amount.setText(currentInventory.getQuantity() + " Available");
                        amount.setVisibility(View.VISIBLE);

                        TextView limited = (TextView) findViewById(R.id.limitedAvalibility);
                        if (currentInventory.getQuantity() > 5) {
                            limited.setTextColor(Color.parseColor("#3BB912"));
                            limited.setVisibility(View.VISIBLE);
                            limited.setText("In Stock");
                        }
                        else if (currentInventory.getQuantity()  > 0 && currentInventory.getQuantity() <= 5) {
                            limited.setTextColor(Color.parseColor("#B99712"));
                            limited.setVisibility(View.VISIBLE);
                            limited.setText("Limited Avalibility");
                        }
                        else if (currentInventory.getQuantity() <= 0) {
                            limited.setTextColor(Color.parseColor("#B92C12"));
                            limited.setVisibility(View.VISIBLE);
                            limited.setText("Out of Stock");
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

    private void defaultMap() {

        TextView storeName = (TextView) findViewById(R.id.storeName);
        TextView storeAddPhone = (TextView) findViewById(R.id.storeAddPhone);
        TextView amount = (TextView) findViewById(R.id.numAvaliable);
        amount.setText("In Stock");
        storeName.setText("FC Rideau Center LCBO");
        storeAddPhone.setText("50 Rideau St" + " • " + "(613) 569-1879");
        LatLng rideau = new LatLng(45.4256461, -75.6958886);
        Marker marker = map.addMarker(new MarkerOptions().position(rideau));
        marker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLng(rideau));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    public List<Store> createStores(JSONObject response) {
        JSONArray jsonArray;
        List<Store> items = new ArrayList<Store>();
        try {
            jsonArray = new JSONArray(response.getString("result"));
            for (int i =0; i<jsonArray.length(); i++) {

                try {
                    items.add(new Store(jsonArray.getJSONObject(i)));
                } catch (NullPointerException e) {
                    continue;
                }
            }
        } catch (JSONException e) {
            return items;
        }
        return items;
    }

    public JSONObject createInventory(JSONObject response) {
        JSONObject inventory = null;
        try {
           inventory = new JSONObject(response.getString("result"));

        } catch (JSONException e) {

        }
        return inventory;
    }

    private void addFont() {

        TextView name = (TextView) findViewById(R.id.nameLabel);
        name.setTypeface(EasyFonts.robotoBold(this));
        TextView cat = (TextView) findViewById(R.id.catLabel);
        cat.setTypeface(EasyFonts.robotoBold(this));
        TextView avalibility = (TextView) findViewById(R.id.limitedAvalibility);
        avalibility.setTypeface(EasyFonts.robotoMedium(this));
        TextView price = (TextView) findViewById(R.id.priceLabel);
        price.setTypeface(EasyFonts.robotoMedium(this));
        TextView volume = (TextView) findViewById(R.id.volumeLabel);
        volume.setTypeface(EasyFonts.robotoRegular(this));

        TextView numAvaliable = (TextView) findViewById(R.id.numAvaliable);
        numAvaliable.setTypeface(EasyFonts.robotoRegular(this));
        TextView storeName = (TextView) findViewById(R.id.storeName);
        storeName.setTypeface(EasyFonts.robotoMedium(this));
        TextView phone = (TextView) findViewById(R.id.storeAddPhone);
        phone.setTypeface(EasyFonts.robotoRegular(this));

    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return DetailsFragment.newInstance(position + 1, productItem);
            }
            return DescriptionFragment.newInstance(position + 1, productItem);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    if (!productItem.getDescription().equals("null")) {
                        return "DESCRIPTION";
                    }
                    return "TASTING NOTE";
            }
            return null;
        }

    }

}
