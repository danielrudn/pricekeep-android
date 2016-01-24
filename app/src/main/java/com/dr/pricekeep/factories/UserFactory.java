package com.dr.pricekeep.factories;

import com.dr.pricekeep.models.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class UserFactory {

    private static final String EMAIL_KEY = "email";

    private UserFactory() { }

    public static User fromAuthData(AuthData data) {
        return new User(data.getUid(), data.getProviderData().get(EMAIL_KEY).toString());
    }

}