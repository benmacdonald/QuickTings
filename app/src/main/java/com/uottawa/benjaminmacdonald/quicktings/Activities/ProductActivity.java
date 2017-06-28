package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.uottawa.benjaminmacdonald.quicktings.Classes.ProductItem;
import com.uottawa.benjaminmacdonald.quicktings.R;

public class ProductActivity extends AppCompatActivity {

    private FABToolbarLayout toolbarLayout;

    String description;
    String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbarLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        View fab = findViewById(R.id.fabtoolbar_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarLayout.show();
            }
        });


        View scrollView = findViewById(R.id.contentView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                toolbarLayout.hide();
            }
        });

        Intent intent = getIntent();
        ProductItem productItem = (ProductItem) intent.getExtras().getParcelable("PRODUCT");

        setupProduct(productItem);
    }

    @Override
    public void onBackPressed() {
        toolbarLayout.hide();
    }

    private void setupProduct(ProductItem productItem) {

        ImageView productImage = (ImageView) findViewById(R.id.productImage);
        Glide.with(this).load(productItem.getImageUrl()).into(productImage);

        TextView titleView = (TextView) findViewById(R.id.titleLabel);
        titleView.setText(productItem.getName());

        TextView catView = (TextView) findViewById(R.id.catLabel);
        catView.setText(productItem.getCategory());

        TextView priceView = (TextView) findViewById(R.id.priceLabel);
        priceView.setText("$"+ productItem.getPrice() / 100.00);

        TextView volumeView = (TextView) findViewById(R.id.volumeLabel);
        volumeView.setText(productItem.getVolume() + " mL " +productItem.getUnitType());

        description = productItem.getDescription();
        details = productItem.getOrigin();

        //TODO: Description returning null
//        TextView textToChange = (TextView) findViewById(R.id.textToChange);
//        textToChange.setText(productItem.getDescription());
    }
}
