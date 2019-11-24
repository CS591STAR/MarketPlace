package com.example.marketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.marketplace.MarketFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class ItemPostForm extends Fragment {

    EditText itemNameTxt;
    EditText itemAskingPriceTxt;
    EditText itemZipcodeTxt;
    Spinner ItemCategoryDropdown;
    Spinner ItemConditionDropDown;
    Button savePostButton;
    ImageView itemImage;

    Button AddImageItemPostButton;
    static final int TAKE_PHOTO = 9999; //A flag that we will use to track the result of an intent later.

    public ItemPostForm() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_post_form, container, false);

        savePostButton = view.findViewById(R.id.savePostButton);
        itemNameTxt = view.findViewById(R.id.itemNameTxt);
        itemAskingPriceTxt = view.findViewById(R.id.itemAskingPriceTxt);
        itemZipcodeTxt = view.findViewById(R.id.itemZipcodeTxt);
        itemImage = view.findViewById(R.id.itemImage);

        AddImageItemPostButton = view.findViewById(R.id.AddImageItemPostButton);

        // Dropdown for the item category
        ItemCategoryDropdown = view.findViewById(R.id.ItemCategoryDropdown);

        ArrayAdapter<String> ItemCategoryDropDownAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        ItemCategoryDropdown.setAdapter(ItemCategoryDropDownAdapter);

        // Dropdown for the item condition
        ItemConditionDropDown = view.findViewById(R.id.ItemConditionDropDown);

        ArrayAdapter<String> ItemConditionDropDownAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.itemConditions));

        ItemConditionDropDown.setAdapter(ItemConditionDropDownAdapter);

        AddImageItemPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }
        });

        savePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long currentTime = System.currentTimeMillis()/1000;
                Post newPost = new Post(itemNameTxt.getText().toString(), Integer.parseInt(String.valueOf(itemAskingPriceTxt.getText())),
                        Integer.parseInt(String.valueOf(itemZipcodeTxt.getText())), ItemCategoryDropdown.getSelectedItem().toString(), ItemConditionDropDown.getSelectedItem().toString(), currentTime.toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (!(resultCode == RESULT_OK)) {
            Toast.makeText(getActivity().getApplicationContext(), "Action to take image has failed", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case TAKE_PHOTO:
                Bundle bundleData = data.getExtras();           //images are stored in a bundle wrapped within the intent...
                Bitmap ItemPhoto = (Bitmap) bundleData.get("data");//the bundle key is "data".  Requires some reading of documentation to remember. :)
                itemImage.setImageBitmap(ItemPhoto);
                itemImage.setVisibility(View.VISIBLE);
                break;
        }
    }
}

