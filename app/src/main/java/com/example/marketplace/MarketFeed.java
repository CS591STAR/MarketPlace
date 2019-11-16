package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MarketFeed extends AppCompatActivity {

    Button testBtn;

    public static class Post {

        String itemName;
        int askingPrice;
        int zipcode;
        String sellerID;
        String imageReference;
        String category;
        String itemCondition;
        int itemPostTime;

        public Post(String itemName, int askingPrice, int zipcode,String sellerID,
                    String imageReference, String category, String itemCondition,
                    int itemPostTime) {
            this.itemName = itemName;
            this.askingPrice = askingPrice;
            this.zipcode = zipcode;
            this.sellerID = sellerID;
            this.imageReference = imageReference;
            this.category = category;
            this.itemCondition = itemCondition;
            this.itemPostTime = itemPostTime;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public void setAskingPrice(int askingPrice) {
            this.askingPrice = askingPrice;
        }

        public void setZipcode(int zipcode) {
            this.zipcode = zipcode;
        }

        public void setSellerID(String sellerID) {
            this.sellerID = sellerID;
        }

        public void setImageReference(String imageReference) {
            this.imageReference = imageReference;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setItemCondition(String itemCondition) {
            this.itemCondition = itemCondition;
        }

        public void setItemPostTime(int itemPostTime) {
            this.itemPostTime = itemPostTime;
        }
    }

    private ListView feedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);

        feedListView = findViewById(R.id.feedListView);
        testBtn = findViewById(R.id.testBtn);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemPostForm.class);
                startActivity(intent);
            }
        });



    }
}
