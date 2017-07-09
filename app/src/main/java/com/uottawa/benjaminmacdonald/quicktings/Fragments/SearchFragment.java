package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uottawa.benjaminmacdonald.quicktings.Activities.ProductActivity;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.ProductItemArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;

    private String query;

    private OnFragmentInteractionListener mListener;
    private RequestQueue requestQueue;
    private List<ProductItem> productItems;
    private ProductItemArrayAdapter productArrayAdapter;

    private TextView numResult;
    private TextView searchParam;

    private int lastPosition;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString("SEARCH_PARAM");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        rootView.requestFocus();

        requestQueue = Volley.newRequestQueue(getActivity());
        updateSearchResults(query);

        ListView listView = (ListView) rootView.findViewById(R.id.productListView);

        numResult = (TextView) rootView.findViewById(R.id.numResults);
        searchParam = (TextView) rootView.findViewById(R.id.searchTerm);

        productItems = new ArrayList<ProductItem>();

        productArrayAdapter = new ProductItemArrayAdapter(getActivity(), productItems);
        listView.setAdapter(productArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long name){
                ProductItem productItem = productArrayAdapter.getItem(position);
                lastPosition = position;

                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("PRODUCT" , productItem);
                startActivityForResult(intent, 1);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ListView listView = (ListView) getView().findViewById(R.id.productListView);
                View card = getViewByPosition(lastPosition, listView);
                ImageButton favouriteButton = (ImageButton) card.findViewById(R.id.favouriteButton);
                if (data.getBooleanExtra("result", false)) {
                    favouriteButton.setColorFilter(Color.parseColor("#D64747"));
                } else {
                    favouriteButton.setColorFilter(Color.parseColor("#AAA9A9"));
                }
            } else {
                //did not get data
            }
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
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

    public void updateSearchResults(String query) {
        this.query = query.replaceAll(" ", "%20");
        String url = "https://lcboapi.com/products?access_key="+getString(R.string.api_key)+"&per_page=100&where_not=is_dead&q="+this.query;
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<ProductItem> tmp = createProductItems(response);
                        productItems.clear();
                        productItems.addAll(tmp);
                        updateFilterBarText();
                        productArrayAdapter.notifyDataSetChanged();

                        Log.d("MYAPP",response.toString());

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYAPP","ERROR" + error);

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public List<ProductItem> createProductItems(JSONObject response) {
        JSONArray jsonArray;
        List<ProductItem> items = new ArrayList<ProductItem>();
        try {
            jsonArray = new JSONArray(response.getString("result"));
            for (int i =0; i<jsonArray.length(); i++) {

                try {
                    items.add(new ProductItem(jsonArray.getJSONObject(i)));
                } catch (NullPointerException e) {
                    continue;
                }
            }
        } catch (JSONException e) {
            return items;
        }
        return items;
    }

    public void updateFilterBarText() {
        numResult.setText(Integer.toString(productItems.size()));
        searchParam.setText("results for "+query.replaceAll("%20"," "));
    }

    public List<ProductItem> sortProductItems(String sortParam) {
        return null;
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

}
