package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.uottawa.benjaminmacdonald.quicktings.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Add the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sign out button
        Button signOut = (Button) findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("Sign out")
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton("CANCEL", null).show();
            }
        });
    }

    //Back button functionality
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getBaseContext(), AuthActivity.class);
        startActivity(intent);
    }
}
