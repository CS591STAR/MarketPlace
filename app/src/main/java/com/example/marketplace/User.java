package com.example.marketplace;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    private String id;
    private String name;
    private String email;
    private String img;
    // Collection of Chats
    // Collection of Posts
    // String university;

    public User(String id, String name, String email, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
    }


    // Getter and Setter methods
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // Parcelling part
    public User(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.id = data[0];
        this.name = data[1];
        this.email = data[2];
        this.img = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id, this.name, this.email, this.img});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
