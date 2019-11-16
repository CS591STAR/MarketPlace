package com.example.marketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.marketplace.MarketFeed;

import androidx.appcompat.app.AppCompatActivity;


public class ItemPostForm extends AppCompatActivity {

//    MarketFeed.Post newPost = new MarketFeed.Post();

    Button AddImageItemPostButton;
    static final int TAKE_PHOTO = 9999; //A flag that we will use to track the result of an intent later.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post_form);

        AddImageItemPostButton = findViewById(R.id.AddImageItemPostButton);

        // Dropdown for the item category

        Spinner ItemCategoryDropdown = findViewById(R.id.ItemCategoryDropdown);

        ArrayAdapter<String> ItemCategoryDropDownAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        ItemCategoryDropdown.setAdapter(ItemCategoryDropDownAdapter);

        // Dropdown for the item condition

        Spinner ItemConditionDropDown = findViewById(R.id.ItemConditionDropDown);

        ArrayAdapter<String> ItemConditionDropDownAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.itemConditions));

        ItemConditionDropDown.setAdapter(ItemConditionDropDownAdapter);

        AddImageItemPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (!(resultCode == RESULT_OK)) {
            Toast.makeText(getApplicationContext(), "Action to take image has failed", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case TAKE_PHOTO:
                Bundle bundleData = data.getExtras();           //images are stored in a bundle wrapped within the intent...
                Bitmap ItemPhoto = (Bitmap) bundleData.get("data");//the bundle key is "data".  Requires some reading of documentation to remember. :)
                break;
        }

    }

}
