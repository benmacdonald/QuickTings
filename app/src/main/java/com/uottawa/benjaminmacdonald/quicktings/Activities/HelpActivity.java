package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uottawa.benjaminmacdonald.quicktings.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button reportAProblemButton = (Button) findViewById(R.id.reportAProblemButton);
        reportAProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ReportAProblemActivity.class));
            }
        });

        Button giveFeedbackButton = (Button) findViewById(R.id.giveFeedbackButton);
        giveFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), GiveFeedbackActivity.class));
            }
        });

        Button contactUsButton = (Button) findViewById(R.id.contactUsButton);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ContactUsActivity.class));
            }
        });
    }

    //Back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
