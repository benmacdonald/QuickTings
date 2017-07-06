package com.uottawa.benjaminmacdonald.quicktings.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Tommy on 2017-07-05.
 */

public class DatabaseUtils {

    //Constructor
    public DatabaseUtils() {

    }

    //Add to favourite
    public void addToFavourite(final int productID) {

        //connect to db
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Integer> favourites = (List<Integer>) dataSnapshot.child("users/"+ user.getUid() + "/favourites/").getValue();

                    favourites.add(productID);

                    database.child("users").child(user.getUid()).child("favourites").setValue(productID);

                    //TODO: fix dis
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                    // for errors
                }
            });

        } else {
            // No user is signed in
        }
    }
}
