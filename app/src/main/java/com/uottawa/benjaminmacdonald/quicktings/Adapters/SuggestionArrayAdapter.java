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

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-06-28.
 */

public class SuggestionArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private final String type;

    public SuggestionArrayAdapter(Context context, List<String> values, String type) {
        super(context, R.layout.card_suggestion_result, values);
        this.context = context;
        this.values = values;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listView = inflater.inflate(R.layout.card_suggestion_result,parent,false);
        if(type.equals("RECENT")) {
            listView = inflater.inflate(R.layout.card_recent_result,parent,false);
        }

        //Set the title of the product
        TextView suggestion = (TextView) listView.findViewById(R.id.suggestion);
        suggestion.setText(values.get(position));

        return listView;
    }
}
