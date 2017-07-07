package com.uottawa.benjaminmacdonald.quicktings.Classes;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tommy on 2017-07-05.
 */

public class DatabaseUtils {

    final private FirebaseDatabase database;
    final private FirebaseUser user;
    final private DatabaseReference favouritesRef;
    private HashMap favourites;

    //Constructor
    public DatabaseUtils() {

        //connect to db
        database = FirebaseDatabase.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //get favourites reference
        favouritesRef = database.getReference("users/"+ user.getUid() + "/favourites");

        //Create an empty HashMap in case user has no favourites and ProductItemArrayAdapter tries to call getFavourites()
        favourites = new HashMap();

        ValueEventListener favouritesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.print("hello");
                if (dataSnapshot.exists()) {
                    favourites = (HashMap) dataSnapshot.getValue();
                } else {
                    //Creates an empty HashMap if a user unfavourites everything
                    favourites = new HashMap();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        favouritesRef.addValueEventListener(favouritesListener);
    }

    //Add to favourite
    public void addToFavourite(final int productID) {

        if (user != null) {
            // User is signed in

            favouritesRef.child(String.valueOf(productID)).setValue(productID);

        } else {
            // No user is signed in
        }
    }

    //Remove from favourite
    public void removeFromFavourite (int productID) {

        if (user != null) {
            //User is signed in

            //get reference for product
            DatabaseReference productRef = database.getReference("users/"+ user.getUid() + "/favourites/" + productID);
            productRef.removeValue();

        } else {
            // No user is signed in
        }
    }

    public HashMap getFavourites() {
        return favourites;
    }
}
