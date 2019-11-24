package com.example.marketplace;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private String itemName;
    private int askingPrice;
    private int zipcode;
    private String sellerID;
    private String category;
    private String itemCondition;
    private String itemPostTime;
    private String itemDescription;

    public Post(String itemName, int askingPrice, int zipcode, String sellerID, String category, String itemCondition,
                String itemPostTime, String itemDescription) {
        this.itemName = itemName;
        this.askingPrice = askingPrice;
        this.zipcode = zipcode;
        this.sellerID = sellerID;
        this.category = category;
        this.itemCondition = itemCondition;
        this.itemPostTime = itemPostTime;
        this.itemDescription = itemDescription;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setAskingPrice(int askingPrice) {
        this.askingPrice = askingPrice;
    }

    public int getAskingPrice(){
        return this.askingPrice;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public int getZipcode(){
        return this.zipcode;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getSellerID(){
        return this.sellerID;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory(){
        return this.category;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getItemCondition(){
        return this.itemCondition;
    }

    public void setItemPostTime(String itemPostTime) {
        this.itemPostTime = itemPostTime;
    }

    public String getItemPostTime(){
        return this.itemPostTime;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Post(Parcel in) {
        String[] postData = new String[8];

        in.readStringArray(postData);
        this.itemName = postData[0];
        this.askingPrice = Integer.parseInt(postData[1]);
        this.zipcode = Integer.parseInt(postData[2]);
        this.sellerID = postData[3];
        this.category = postData[4];
        this.itemCondition = postData[5];
        this.itemPostTime = postData[6];
        this.itemDescription = postData[7];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.itemName, String.valueOf(this.askingPrice), String.valueOf(this.zipcode),
                 this.category, this.itemCondition, String.valueOf(this.itemPostTime)});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
