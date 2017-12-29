package com.torfin.mybulletjournal.dataobjects;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by torftorf1 on 12/4/17.
 */

@IgnoreExtraProperties
public class User {

    public String email;

    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
