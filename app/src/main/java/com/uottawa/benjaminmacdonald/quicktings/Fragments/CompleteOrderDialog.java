package com.uottawa.benjaminmacdonald.quicktings.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.quicktings.Activities.ConfirmationActivity;
import com.uottawa.benjaminmacdonald.quicktings.Classes.OrdersCart;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;
import com.uottawa.benjaminmacdonald.quicktings.Interfaces.CompletionCallable;
import com.uottawa.benjaminmacdonald.quicktings.R;

import java.util.ArrayList;

/**
 * Created by thomas on 18/07/17.
 */

public class CompleteOrderDialog extends AppCompatDialogFragment implements CompletionCallable {

    View.OnClickListener cancelOrder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().cancel();
        }
    };

    private View rootView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.complete_dialog, null);
        builder.setView(rootView);

        LinearLayout orderLayout = (LinearLayout) rootView.findViewById(R.id.dialog_order_list);

        ArrayList<ShoppingCart.CartItem> list = new ArrayList<>(ShoppingCart.getInstance().getCart());
        for (int i = 0; i < list.size(); i++) {
            ShoppingCart.CartItem item = list.get(i);
            inflater.inflate(R.layout.card_dialog_item, orderLayout, true);
            View child = orderLayout.getChildAt(i);
            ((TextView) child.findViewById(R.id.dialog_item_name)).setText(item.item_name);
            ((TextView) child.findViewById(R.id.dialog_item_quantity)).setText(item.quantity);
        }

        ((TextView) rootView.findViewById(R.id.dialog_order_total)).setText(ShoppingCart.getInstance().getTotalBill());

        rootView.findViewById(R.id.close_confirmation).setOnClickListener(cancelOrder);
        rootView.findViewById(R.id.cancel_order).setOnClickListener(cancelOrder);
        rootView.findViewById(R.id.confirm_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().setCancelable(false);
                rootView.findViewById(R.id.order_wrapper).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.waiting_wrapper).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.close_confirmation).setVisibility(View.GONE);
                OrdersCart.getInstance().addCurrentOrder();
            }
        });

        return builder.create();
    }

    @Override
    public void onComplete(Object item, int resultCode) {

    }

    @Override
    public void onFinalize() {
        getDialog().dismiss();
        startActivity(new Intent(getActivity(), ConfirmationActivity.class));
    }
}
