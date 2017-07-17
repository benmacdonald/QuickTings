package com.uottawa.benjaminmacdonald.quicktings.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uottawa.benjaminmacdonald.quicktings.Classes.CreditCard;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2017-07-14.
 */

public class CreditCardAdapter extends ArrayAdapter<CreditCard> {

    private final Context context;
    private final List<CreditCard> values;

    public CreditCardAdapter(Context context, List<CreditCard> values) {
        super(context, R.layout.card_credit, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.card_credit,parent,false);

        TextView cardName = (TextView) listView.findViewById(R.id.cardName);
        cardName.setText(values.get(position).getCardNumber());

        TextView cardDate = (TextView) listView.findViewById(R.id.cardDate);
        cardDate.setText(values.get(position).getExpire());

        ImageView cardType = (ImageView) listView.findViewById(R.id.cardType);
        if (values.get(position).getType().equals("VISA")) {
            Glide.with(context).load("https://simplelink.simplepay.ng/wp-content/uploads/2015/10/visa-logo-white.png").into(cardType);
        }
        else if (values.get(position).getType().equals("MASTER")) {
            Glide.with(context).load("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/MasterCard_Logo.svg/1280px-MasterCard_Logo.svg.png").into(cardType);
        }
        else if (values.get(position).getType().equals("AMERICAN")) {
            Glide.with(context).load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/American_Express_logo.svg/2000px-American_Express_logo.svg.png").into(cardType);
        }


        return listView;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }
}
