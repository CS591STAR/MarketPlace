package com.example.marketplace;

import java.util.HashMap;


public class User {

    private String id;
    private String name;
    private String email;
    private String img;
    private String uni;
    private double rating;
    private double numRatings;
    private String zip;


    public User(String id, String name, String email, String img, String uni, double rating, double numRatings, String zip) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
        this.uni = uni;
        this.rating = rating;
        this.zip = zip;
        this.numRatings = numRatings;
    }

    public User(HashMap<String, Object> user) {
        this.id = user.get("id").toString();
        this.name = user.get("name").toString();
        this.email = user.get("email").toString();
        this.img = user.get("img").toString();
        this.uni = user.get("uni").toString();
        this.rating = Double.parseDouble(user.get("itemCondition").toString());
        this.zip = user.get("zip").toString();
        this.numRatings = Double.parseDouble(user.get("itemDescription").toString());
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

    public String getUni() {
        return this.uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip() {
        this.zip = zip;
    }

    public double getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(double numRatings) {
        this.numRatings = numRatings;
    }
}
