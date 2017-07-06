package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tommy on 2017-07-05.
 */

public class DatabaseUtils {

    final private FirebaseDatabase database;
    final private FirebaseUser user;

    //Constructor
    public DatabaseUtils() {

        //connect to db
        database = FirebaseDatabase.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

    }

    //Add to favourite
    public void addToFavourite(final int productID) {

        if (user != null) {
            // User is signed in

            //get reference for favourites
            DatabaseReference favouritesRef = database.getReference("users/"+ user.getUid() + "/favourites/");

            if (favouritesRef == null) {
                favouritesRef = database.getReference("users/"+ user.getUid());
                favouritesRef.setValue("/favourites/" + productID);
            } else {
                favouritesRef.push().setValue(productID);
            }

        } else {
            // No user is signed in
        }
    }
}
