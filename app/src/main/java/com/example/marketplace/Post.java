package com.example.marketplace;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

public class Post implements Parcelable {

    public enum Category{
        TEXTBOOK,
        FURNITURE,
        ELECTRONICS,
        OTHER;
    }

    public enum Condition{
        GREAT,
        GOOD,
        DECENT,
        POOR
    }

    private String itemName;
    private long askingPrice;
    private String eBayPrice;
    private String amazonPrice;
    private String zipcode;
    private String sellerID;
    private String category;
    private String itemCondition;
    private long itemPostTime;
    private String itemDescription;
    private String postID;

    public String geteBayPrice() {
        return eBayPrice;
    }

    public void seteBayPrice(String eBayPrice) {
        this.eBayPrice = eBayPrice;
    }

    public String getAmazonPrice() {
        return amazonPrice;
    }

    public void setAmazonPrice(String amazonPrice) {
        this.amazonPrice = amazonPrice;
    }

    private String image;

    public Post(String itemName, long askingPrice, String zipcode, String sellerID, String category, String itemCondition,
                long itemPostTime, String itemDescription, String postID, String image, String eBayPrice, String amazonPrice) {
        this.itemName = itemName;
        this.askingPrice = askingPrice;
        this.zipcode = zipcode;
        this.sellerID = sellerID;
        this.category = category;
        this.itemCondition = itemCondition;
        this.itemPostTime = itemPostTime;
        this.itemDescription = itemDescription;
        this.postID = postID;
        this.image = image;
        this.eBayPrice = eBayPrice;
        this.amazonPrice = amazonPrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setAskingPrice(long askingPrice) {
        this.askingPrice = askingPrice;
    }

    public long getAskingPrice(){
        return this.askingPrice;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipcode(){
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

    public void setItemPostTime(long itemPostTime) {
        this.itemPostTime = itemPostTime;
    }

    public long getItemPostTime(){
        return this.itemPostTime;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Post(Parcel in) {
        String[] postData = new String[10];
        in.readStringArray(postData);
        this.itemName = postData[0];
        this.askingPrice = Integer.parseInt(postData[1]);
        this.zipcode = postData[2];
        this.sellerID = postData[3];
        this.category = postData[4];
        this.itemCondition = postData[5];
        this.itemPostTime = Long.parseLong(postData[6]);
        this.itemDescription = postData[7];
        this.postID = postData[8];
        this.image = postData[9];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.itemName, String.valueOf(this.askingPrice), String.valueOf(this.zipcode),
                 this.sellerID, this.category, this.itemCondition, String.valueOf(this.itemPostTime), this.itemDescription, this.postID, this.image.toString()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
