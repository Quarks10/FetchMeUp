package com.urbantechies.fetch_me_up;

import android.app.Application;

import com.urbantechies.fetch_me_up.model.User;


public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
