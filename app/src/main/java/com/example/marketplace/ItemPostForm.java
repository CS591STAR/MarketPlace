package com.example.marketplace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.CAMERA_SERVICE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ItemPostForm extends Fragment {

    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 1;

    private StorageReference storageReference;

    private FirebaseFirestore db;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private static final String TAG = "NEWPOST";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    OkHttpClient client = new OkHttpClient();


    EditText itemNameTxt;
    EditText itemAskingPriceTxt;
    EditText itemZipcodeTxt;
    Spinner ItemCategoryDropdown;
    Spinner ItemConditionDropDown;
    Button savePostButton;
    ImageView itemImage;
    Bitmap postImage;
    EditText postDescriptionText;
    long currentTime = 0;

    Button AddImageItemPostButton;
    String postID;
    String imageLink;

    public ItemPostForm() {

    }

    public interface ItemPostFormListener {
        public void openFeed();
        // add methods that we would need the activity to implement
    }

    ItemPostFormListener IPFL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IPFL = (ItemPostFormListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_post_form, container, false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        mUsername = mFirebaseUser.getUid();

        // to save every post to firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        postID = mDatabase.push().getKey();

        savePostButton = view.findViewById(R.id.savePostButton);
        itemNameTxt = view.findViewById(R.id.itemNameTxt);
        itemAskingPriceTxt = view.findViewById(R.id.itemAskingPriceTxt);
        itemZipcodeTxt = view.findViewById(R.id.itemZipcodeTxt);
        itemImage = view.findViewById(R.id.itemImage);
        postDescriptionText = view.findViewById(R.id.postDescriptionText);

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
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    Log.i("here: ", "permission has been granted");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE);
                }
            }
        });

        savePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                try {
                    currentTime = doGetRequest();
                } catch (IOException e) {
                    Log.i("HTTP_CALL", "didn't go through");
                    Date date = new Date();
                    currentTime = date.getTime();
                }

//                if (TextUtils.isEmpty(itemNameTxt.getText()) && TextUtils.isEmpty(itemAskingPriceTxt.getText()) && TextUtils.isEmpty(itemZipcodeTxt.getText()) && TextUtils.isEmpty(postDescriptionText.getText()) && (itemImage != null)) {
                    //get first 3 words in item name and search it in eBay
                EBayAPI eBayAPI = EBayAPI.getInstance();
                String itemName = itemNameTxt.getText().toString();
                Log.w(TAG, firstWords(itemName, 3));
                eBayAPI.searchItem(firstWords(itemName, 3));

                AmazonAPI amazonAPI = AmazonAPI.getInstance();
                amazonAPI.searchItem(itemNameTxt.getText().toString());

                Post post = new Post(itemNameTxt.getText().toString(), Long.parseLong(String.valueOf(itemAskingPriceTxt.getText())),
                        itemZipcodeTxt.getText().toString(), mUsername, Post.Category.values()[ItemCategoryDropdown.getSelectedItemPosition()].toString(),
                        Post.Condition.values()[ItemConditionDropDown.getSelectedItemPosition()].toString(), currentTime,
                        postDescriptionText.getText().toString(), postID, "", "", "");

                if (postImage == null) {

                    Toast.makeText(getActivity(), "You need to add a picture!", Toast.LENGTH_LONG).show();

                }

                else {

                    uploadToCloud(postImage, post);
                    Toast.makeText(view.getContext(), "New post created", Toast.LENGTH_SHORT).show();
                    IPFL.openFeed();
                }

            }
        });

        return view;
    }

    //fetch the first n words in the input string
    private String firstWords(String input, int words) {
        for (int i = 0; i < input.length(); i++) {
            // When a space is encountered, reduce words remaining by 1.
            if (input.charAt(i) == ' ') {
                words--;
            }
            // If no more words remaining, return a substring.
            if (words == 0) {
                return input.substring(0, i);
            }
        }
        //string contains less than n words
        if(words != 0){
            return input;
        }
        // Error case.
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Log.i("here: ", "result code is ok");

            postImage = (Bitmap) data.getExtras().get("data");

            // display image
            GlideApp.with(this /* context */)
                    .load(postImage)
                    .into(itemImage);

            itemImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE);

                } else {
                    Toast.makeText(getContext(), "result code not ok?", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void uploadToCloud(Bitmap image, final Post post) {

        Uri uri = getImageUri(getActivity().getApplicationContext(), image);
        // upload image to cloud
        final StorageReference filepath = storageReference.child("Photos").child(postID).child(uri.getLastPathSegment());

        UploadTask uploadTask = filepath.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    imageLink = task.getResult().toString();
                    Log.i("IMG", "download image at " + imageLink);
                    post.setImage(imageLink);

                    if (AmazonAPI.getAmazonPrice() != null){
                        Log.w("amznAPI", "Amazon price got");
                        post.setAmazonPrice(AmazonAPI.getAmazonPrice());
                    } else {
                        Log.w("amznAPI", "Amazon price not got");
                    }

                    if(EBayAPI.geteBayPrice() != null){
                        post.seteBayPrice(EBayAPI.geteBayPrice());
                        Log.w(TAG, "eBay price got");
                    }
                    else{
                        Log.w(TAG, "eBay price not got");
                    }

                    mDatabase.child(postID).setValue(post);
                    Log.i("IMGPOST", "download image at " + post.getImage());

                    // store every post relative to zipcode
                    FirebaseDatabase.getInstance().getReference().child("zipcodes").child(String.valueOf(post.getZipcode())).child(postID).setValue(0);



                } else {
                    // Handle failures
                    // ...
                    Log.i("IMG", "could not upload image");
                }
            }
        });
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

//    private void writeNewPost(String itemName, int askingPrice, String zipcode, String sellerID, String category, String itemCondition,
//                              Long itemPostTime, String itemDescription, String image) {
//
//        Post post = new Post(itemName, askingPrice, zipcode, sellerID, category, itemCondition, itemPostTime, itemDescription, postID, image);
//        mDatabase.child(postID).setValue(post);
//
//        // store every post relative to zipcode
//        FirebaseDatabase.getInstance().getReference().child("zipcodes").child(String.valueOf(post.getZipcode())).child(postID).setValue(0);
//    }

    long doGetRequest() throws IOException {
        Request request = new Request.Builder()
                .url("https://us-central1-marketplace-60dac.cloudfunctions.net/timestamp")
                .build();

        Response response = client.newCall(request).execute();
        return Long.parseLong(response.body().string());
    }
}



