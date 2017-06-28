package com.uottawa.benjaminmacdonald.quicktings.Classes;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by BenjaminMacDonald on 2017-06-27.
 */

@IgnoreExtraProperties
public class User {

    private String email;
    private String firstName;
    private String lastName;
    private String photo;

    public User() {
        //this is required idk why
    }

    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public User(String email, String firstName, String lastName, String photo) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
