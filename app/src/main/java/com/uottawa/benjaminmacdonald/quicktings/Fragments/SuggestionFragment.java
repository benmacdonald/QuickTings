package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Activities.MainActivity;
import com.uottawa.benjaminmacdonald.quicktings.Activities.ProductActivity;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.DiscoverArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.MainActivityArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.SuggestionArrayAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by BenjaminMacDonald on 2017-06-28.
 */


public class SuggestionFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private ArrayList<String> suggestions;

    private SuggestionArrayAdapter arrayAdapter;

    private SearchFragment searchFragment;

    public SuggestionFragment() {

    }

    public static SuggestionFragment newInstance(String param1, String param2) {
        SuggestionFragment fragment = new SuggestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            suggestions = getArguments().getStringArrayList("SUGGESTION_ARRAY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_suggestions, container, false);

        arrayAdapter = new SuggestionArrayAdapter(getActivity(), suggestions);

        ListView listView = (ListView) rootView.findViewById(R.id.suggestionsListView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView labelView = (TextView) view.findViewById(R.id.suggestion);
                ((MainActivity) getActivity()).clickOnSuggested(labelView.getText().toString());
            }
        });

        return rootView;
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
        if (context instanceof MainFragment.OnFragmentInteractionListener) {
            mListener = (SuggestionFragment.OnFragmentInteractionListener) context;
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

    public void updateSuggestions(ArrayList<String> newSuggestions) {
        suggestions.clear();
        suggestions.addAll(newSuggestions);
        arrayAdapter.notifyDataSetChanged();
    }
}
