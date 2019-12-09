package com.example.marketplace;

import java.util.HashMap;
import java.util.Map;

public class Post {

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
    private String zipcode;
    private String sellerID;
    private String category;
    private String itemCondition;
    private long itemPostTime;
    private String itemDescription;
    private String postID;
    private String image;

    public Post(String itemName, long askingPrice, String zipcode, String sellerID, String category, String itemCondition,
                long itemPostTime, String itemDescription, String postID, String image) {
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

    public Post(HashMap<String, Object> post) {

        this.itemName = post.get("itemName").toString();
        this.askingPrice = Long.parseLong(post.get("askingPrice").toString());
        this.zipcode = post.get("zipcode").toString();
        this.sellerID = post.get("sellerID").toString();
        this.category = post.get("category").toString();
        this.itemCondition = post.get("itemCondition").toString();
        this.itemPostTime = Long.parseLong(post.get("itemPostTime").toString());
        this.itemDescription = post.get("itemDescription").toString();
        this.postID = post.get("postID").toString();
        this.image = post.get("image").toString();
    }

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
