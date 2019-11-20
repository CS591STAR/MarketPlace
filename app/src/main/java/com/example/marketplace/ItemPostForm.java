package com.example.marketplace;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;


public class ItemPostForm extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 2;


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
    static final int TAKE_PHOTO = 9999; //A flag that we will use to track the result of an intent later.

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post_form);

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
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (!(resultCode == RESULT_OK)) {
//            Toast.makeText(getApplicationContext(), "Action to take image has failed", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                Bundle bundleData = data.getExtras();           //images are stored in a bundle wrapped within the intent...
//                Bitmap ItemPhoto = (Bitmap) bundleData.get("data");//the bundle key is "data".  Requires some reading of documentation to remember. :)
//                itemImage.setImageBitmap(ItemPhoto);
//                itemImage.setVisibility(View.VISIBLE);
//                String key = databaseReference.getKey();
//                StorageReference storageReference =
//                        FirebaseStorage.getInstance()
//                                .getReference(mFirebaseUser.getUid())
//                                .child(key)
//                                .child(uri.getLastPathSegment());
//                putImageInStorage();
//
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            itemImage.setVisibility(View.VISIBLE);
            Uri uri = data.getData();
            Log.d(TAG, "Uri: " + uri.toString());
            StorageReference storageReference =
                    FirebaseStorage.getInstance()
                            .getReference(mFirebaseUser.getUid())
                            .child("RANDOM")
                            .child(uri.getLastPathSegment());

            putImageInStorage(storageReference, uri, "RANDOM");
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(ItemPostForm.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getReference().getDownloadUrl()
                                    .addOnCompleteListener(ItemPostForm.this,
                                            new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        postImage = task.getResult().toString();
                                                    }
                                                }
                                            });
                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }


}

