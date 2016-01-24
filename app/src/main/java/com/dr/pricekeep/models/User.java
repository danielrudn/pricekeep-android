package com.dr.pricekeep.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class User implements Parcelable {

    private UUID uuid;
    private String email;

    public static final String TAG = User.class.getSimpleName();
    public static Parcelable.Creator<User> CREATOR =
            new Parcelable.Creator<User>() {

                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source.readString(), source.readString());
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };

    public User(String uuid, String email) {
        this.uuid = UUID.fromString(uuid);
        this.email = email;
    }

    public String getUUID() {
        return uuid.toString();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid.toString());
        dest.writeString(email);
    }
}