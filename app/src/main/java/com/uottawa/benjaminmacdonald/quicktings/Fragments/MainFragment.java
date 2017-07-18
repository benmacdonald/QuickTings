package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.benjaminmacdonald.quicktings.Activities.MainActivity;
import com.uottawa.benjaminmacdonald.quicktings.Activities.ProductActivity;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.DiscoverArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.MainActivityArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.DatabaseUtils;
import com.uottawa.benjaminmacdonald.quicktings.Classes.Orders;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.DatabaseCallback;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements DatabaseCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<ProductItem> discoverItems; //TODO: what are we doing with these?
    private List<ProductItem> orderAgainItems;
    private List<ProductItem> favouriteItems;

    private HashMap<String, HashMap<String,Object>> ordersHashMap;

//    private HashMap favourites;

    //For favourites
    private MainActivityArrayAdapter favouritesArrayAdapter;
    private MainActivityArrayAdapter orderAgainArrayAdapter;
    private GridView favouritesView;
    private GridView orderAgainView;

    //Utilities
    private DatabaseUtils databaseUtils;
    private RequestQueue requestQueue;
    private MainActivity mainActivity;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // add fonts
        addFont(rootView);

        // Utilities
        requestQueue = Volley.newRequestQueue(getActivity());
        databaseUtils = new DatabaseUtils(this);
        mainActivity = (MainActivity) getActivity();

        //Discover Gallery
        GridView discoverView = (GridView) rootView.findViewById(R.id.discoverView);
        discoverView.setDrawSelectorOnTop(true);

        CardView discover1 = (CardView) rootView.findViewById(R.id.discover1);
        discover1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.searchFromElsewhere("beau's");
            }
        });

        ImageView discover1Image = (ImageView) rootView.findViewById(R.id.discover1Image);
        discover1Image.setScaleType(ImageView.ScaleType.FIT_XY);

        CardView discover2 = (CardView) rootView.findViewById(R.id.discover2);
        discover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.searchFromElsewhere("gin smash");
            }
        });

        ImageView discover2Image = (ImageView) rootView.findViewById(R.id.discover2Image);
        discover2Image.setScaleType(ImageView.ScaleType.FIT_XY);

        CardView discover3 = (CardView) rootView.findViewById(R.id.discover3);
        discover3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.searchFromElsewhere("malibu");
            }
        });

        ImageView discover3Image = (ImageView) rootView.findViewById(R.id.discover3Image);
        discover3Image.setScaleType(ImageView.ScaleType.FIT_XY);

        //Order Again Product Item List
        orderAgainItems = new ArrayList<ProductItem>();

        orderAgainView = (GridView) rootView.findViewById(R.id.orderAgainView);

        orderAgainView.setEmptyView(rootView.findViewById(R.id.empty_oa_view));

        orderAgainArrayAdapter = new MainActivityArrayAdapter(getActivity(), orderAgainItems);
        orderAgainView.setAdapter(orderAgainArrayAdapter);
        orderAgainView.setNumColumns(orderAgainItems.size());
        if (orderAgainItems.size() > 0) {
            setDynamicWidth(orderAgainView);
        }
        orderAgainView.setDrawSelectorOnTop(true);
        orderAgainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), ProductActivity.class);
                intent.putExtra("PRODUCT", orderAgainItems.get(position).getId());
                startActivity(intent);
            }
        });


        //Favourites ProductItem List
        favouriteItems = new ArrayList<ProductItem>();

        favouritesArrayAdapter = new MainActivityArrayAdapter(getActivity(), favouriteItems);

        favouritesView = (GridView) rootView.findViewById(R.id.favouritesView);

        favouritesView.setEmptyView(rootView.findViewById(R.id.empty_fav_view));

        favouritesView.setAdapter(favouritesArrayAdapter);

        favouritesView.setDrawSelectorOnTop(true);

        favouritesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("PRODUCT", favouriteItems.get(position));
                startActivityForResult(intent, 1);
            }
        });

        ordersHashMap = new HashMap<>();
        getOrders();

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                favouritesArrayAdapter.clear();
                callback(databaseUtils.getFavouritesHashMap());
            } else {
                //did not get data
            }
        }
    }

    @Override
    public void callback (HashMap hm) {

        for (Iterator i = hm.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry favourite = (Map.Entry) i.next();
            String url = "https://lcboapi.com/products/" + favourite.getKey() + "/?access_key=" + getString(R.string.api_key);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                JSONObject product = response.getJSONObject("result");

                                List<ProductItem> tmp = new ArrayList<ProductItem>(favouriteItems);
                                tmp.add(new ProductItem(product));
                                favouriteItems.clear();
                                favouriteItems.addAll(tmp);
                                favouritesArrayAdapter.notifyDataSetChanged();
                                favouritesView.setNumColumns(favouriteItems.size());
                                if (favouriteItems.size() > 0) {
                                    setDynamicWidth(favouritesView);
                                }
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
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Sets the amount of rows depending on the amount of elements.
     * Used for horizontal scrolling.
     * Method taken from stackoverflow.
     * source: http://stackoverflow.com/questions/5725745/horizontal-scrolling-grid-view
     * @param gridView is the layout for the horizontal scrolling.
     */
    private void setDynamicWidth(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }
        int totalWidth;
        int items = gridViewAdapter.getCount();
        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalWidth = listItem.getMeasuredWidth();
        totalWidth = totalWidth * items;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = totalWidth;
        gridView.setLayoutParams(params);
    }

    private void addFont(View rootview) {

        // section labels
        TextView discover = (TextView) rootview.findViewById(R.id.discoverTitle);
        discover.setTypeface(EasyFonts.robotoRegular(getActivity()));
        TextView orderAgain = (TextView) rootview.findViewById(R.id.orderAgainTitle);
        orderAgain.setTypeface(EasyFonts.robotoRegular(getActivity()));
        TextView fav = (TextView) rootview.findViewById(R.id.favouritesTitle);
        fav.setTypeface(EasyFonts.robotoRegular(getActivity()));
    }

    public void getOrders() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRef = database.getReference("orders/"+ user.getUid());

        // Attach a listener to read the data at our posts reference
        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Orders> tmp = new ArrayList<Orders>();
                ordersHashMap = (HashMap<String, HashMap<String,Object>>) dataSnapshot.getValue();

                if (ordersHashMap == null) {
                    ordersHashMap = new HashMap<String, HashMap<String, Object>>();
                }
                for ( Map.Entry<String, HashMap<String,Object>> entry : ordersHashMap.entrySet()) {
                    String key = entry.getKey();
                    HashMap<String, Object> item = entry.getValue();
                    Orders order = new Orders();
                    order.setOrder_cost((Double) item.get("order_cost"));
                    order.setOrder_date((String) item.get("order_date"));
                    order.setLatitude((Double) item.get("latitude"));
                    order.setLongitude((Double) item.get("longitude"));
                    order.setProducts((List<Integer>) item.get("products"));

                    tmp.add(order);
                }

                for (Orders orders : tmp) {
                    ArrayList<Integer> test = new ArrayList<Integer>();
                    test.addAll(orders.getProducts());
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
                                            List<ProductItem> tmp = new ArrayList<ProductItem>(orderAgainItems);
                                            tmp.add(new ProductItem(product));
                                            orderAgainItems.clear();
                                            orderAgainItems.addAll(tmp);
                                            orderAgainArrayAdapter.notifyDataSetChanged();
                                            orderAgainView.setNumColumns(orderAgainItems.size());
                                            if (orderAgainItems.size() > 0) {
                                                setDynamicWidth(orderAgainView);
                                            }

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
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}
