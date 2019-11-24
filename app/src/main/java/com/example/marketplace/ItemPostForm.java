package com.example.marketplace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.marketplace.MarketFeed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


public class ItemPostForm extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 1;

    private StorageReference storageReference;


    private static final String TAG = "NEWPOST";
    EditText itemNameTxt;
    EditText itemAskingPriceTxt;
    EditText itemZipcodeTxt;
    Spinner ItemCategoryDropdown;
    Spinner ItemConditionDropDown;
    Button savePostButton;
    ImageView itemImage;
    String postImage;

    Button AddImageItemPostButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post_form);

        storageReference = FirebaseStorage.getInstance().getReference();
        mUsername = ANONYMOUS;
        savePostButton = findViewById(R.id.savePostButton);
        itemNameTxt = findViewById(R.id.itemNameTxt);
        itemAskingPriceTxt = findViewById(R.id.itemAskingPriceTxt);
        itemZipcodeTxt = findViewById(R.id.itemZipcodeTxt);
        itemImage = findViewById(R.id.itemImage);

        AddImageItemPostButton = findViewById(R.id.AddImageItemPostButton);

        // Dropdown for the item category
        ItemCategoryDropdown = findViewById(R.id.ItemCategoryDropdown);

        ArrayAdapter<String> ItemCategoryDropDownAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));

        ItemCategoryDropdown.setAdapter(ItemCategoryDropDownAdapter);

        // Dropdown for the item condition
        ItemConditionDropDown = findViewById(R.id.ItemConditionDropDown);

        ArrayAdapter<String> ItemConditionDropDownAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.itemConditions));

        ItemConditionDropDown.setAdapter(ItemConditionDropDownAdapter);

        AddImageItemPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        });

        savePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                Post newPost = new Post(itemNameTxt.getText().toString(), Integer.parseInt(String.valueOf(itemAskingPriceTxt.getText())),
                        Integer.parseInt(String.valueOf(itemZipcodeTxt.getText())), ItemCategoryDropdown.getSelectedItem().toString(), ItemConditionDropDown.getSelectedItem().toString(), currentTime.toString());
            }
        });

    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in.
//        if (mFirebaseUser == null) {
//            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//            return;
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageUri(getApplicationContext(), photo);

            StorageReference filepath = storageReference.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"upload worked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}

