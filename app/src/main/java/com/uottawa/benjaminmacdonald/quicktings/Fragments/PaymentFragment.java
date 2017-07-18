package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.uottawa.benjaminmacdonald.quicktings.Activities.CheckoutActivity;
import com.uottawa.benjaminmacdonald.quicktings.Adapters.CreditCardAdapter;
import com.uottawa.benjaminmacdonald.quicktings.Classes.CreditCard;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BenjaminMacDonald on 2017-07-12.
 */

public class PaymentFragment extends Fragment implements Step {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;

    GridView creditCardView;

    public PaymentFragment() {}

    public static PaymentFragment newInstance(int sectionNumber) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        //shared preferences

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_payment, container, false);

        final List<CreditCard> cards = new ArrayList<>();
        cards.add(new CreditCard("Benjamin MacDonald","4730034470933407", "0220", "455", "VISA"));
        cards.add(new CreditCard("Benjamin MacDonald", "5392789319203595", "0325", "455", "MASTER"));
        cards.add(new CreditCard("Benjamin MacDonald", "347449635398431", "0325", "4555", "AMERICAN"));
        CreditCardAdapter creditCardAdapter = new CreditCardAdapter(getActivity(), cards);
        creditCardView = (GridView) rootView.findViewById(R.id.creditCardView);

        creditCardView.setAdapter(creditCardAdapter);
        creditCardView.setNumColumns(cards.size());
        this.setDynamicWidth(creditCardView);


        creditCardView.setDrawSelectorOnTop(true);
        creditCardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CreditCard card = cards.get(i);

                EditText cardHolderName = (EditText) rootView.findViewById(R.id.cardHolderName);
                cardHolderName.setText(card.getName());

                EditText cardNumber = (EditText) rootView.findViewById(R.id.cardNumber);
                cardNumber.setText(card.getCardNumber());

                EditText expiryDate = (EditText) rootView.findViewById(R.id.cardExpire);
                expiryDate.setText(card.getExpire());

                EditText cvv = (EditText) rootView.findViewById(R.id.cardCVV);
                cvv.setText(card.getCvv());
            }
        });

        // card verification
        cardVerification(rootView);


        return rootView;
    }

    @Override
    public VerificationError verifyStep() {

        boolean isValid = true;
        EditText cardName = (EditText) rootView.findViewById(R.id.cardHolderName);
        EditText cardNumber = (EditText) rootView.findViewById(R.id.cardNumber);
        EditText cardExpire = (EditText) rootView.findViewById(R.id.cardExpire);
        EditText cardCvv = (EditText) rootView.findViewById(R.id.cardCVV);

        if(cardName.getText().toString().isEmpty()) {
            cardName.setError("cardholder name cannot be empty");
            isValid = false;
        }
        if (cardNumber.getText().toString().isEmpty()) {
            cardNumber.setError("card number cannot be empty");
            isValid = false;
        }
        if (cardExpire.getText().toString().isEmpty()) {
            cardExpire.setError("expiry date cannot be empty");
            isValid = false;
        }
        if (cardCvv.getText().toString().isEmpty()) {
            cardCvv.setError("cvv cannot be empty");
            isValid = false;
        }

        if (!isValid) {
            return new VerificationError("one of the fields is empty");
        }


        return null;
    }

    @Override
    public void onSelected() {
        Log.w("","");
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void cardVerification(final View rootView) {
        MaterialEditText cardNumber = (MaterialEditText) rootView.findViewById(R.id.cardNumber);

        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                MaterialEditText cvv = (MaterialEditText) rootView.findViewById(R.id.cardCVV);

                // CARD MATCHES VISA CARD
                Pattern visa = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
                Matcher visaMatch = visa.matcher(charSequence);

                // CARD MATCHES MASTER CARD
                Pattern masterCard = Pattern.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$");
                Matcher masterCardMatch = masterCard.matcher(charSequence);

                // CARD MATCHES AMERICAN EXPRESS
                Pattern americanExpress = Pattern.compile("^3[47]\\d{13}$");
                Matcher americanExpressMatch = americanExpress.matcher(charSequence);
                if (visaMatch.matches()) {
                    //is visa
                    cvv.setMaxCharacters(3);
                }

                else if (masterCardMatch.matches()) {
                    //is master card
                    cvv.setMaxCharacters(3);
                }

                else if (americanExpressMatch.matches()) {
                    //is american express
                    cvv.setMaxCharacters(4);
                }
                else {
                    MaterialEditText cardNum = (MaterialEditText) rootView.findViewById(R.id.cardNumber);
                    cardNum.setError("not a valid card number");
                    cvv.setMaxCharacters(3);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });


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
}
