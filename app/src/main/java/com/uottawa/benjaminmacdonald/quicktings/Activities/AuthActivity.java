package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawa.benjaminmacdonald.quicktings.Classes.User;
import com.uottawa.benjaminmacdonald.quicktings.R;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //Hide action bar
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        Button guestButton = (Button) findViewById(R.id.guestButton);

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAsGuest();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            //transition to main screen since the user has already logged in before
            setPrefId(currentUser.getUid());

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void signInAsGuest() {
        this.mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    setPrefId(currentUser.getUid());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    User user = new User(null, "Guest", "User");
                    String id = currentUser.getUid();
                    ref.child("users").child(id).setValue(user);

                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(AuthActivity.this, "Authentication Succeeded.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AuthActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setPrefId(String userId) {

        SharedPreferences settings = getSharedPreferences(getString(R.string.preferences), 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(getString(R.string.user_id), userId);
        editor.commit();
    }



}
