package com.example.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class ViewPost extends Fragment {

    Button deletePost;
    Button contactSeller;
    TextView postTitle;
    TextView postPrice;
    TextView postDescription;
    TextView postCondition;
    ImageView postImage;
    Post post;

    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private DatabaseReference mDatabase;


    public ViewPost(){
        //Required empty public constructor
    }

    public ViewPost(Post post){
        this.post = post;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_post, container, false);


        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");


        deletePost = view.findViewById(R.id.DeletePost);
        contactSeller = view.findViewById(R.id.contactSellerPost);
        postTitle = view.findViewById(R.id.titlePost);
        postPrice = view.findViewById(R.id.pricePost);
        postCondition = view.findViewById(R.id.conditionPost);
        postDescription = view.findViewById(R.id.descriptionPost);
        postImage = view.findViewById(R.id.imagePost);


        // set post info
        postTitle.setText(post.getItemName());
        postDescription.setText(post.getItemDescription());
        postCondition.setText(post.getItemCondition());
//        postImage.setImageBitmap(post.get);
        postPrice.setText(Long.toString(post.getAskingPrice()));


        // update UI
        updateUI();

        contactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat();
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePostFromDB();
            }
        });

        return view;
    }

    private void startChat() {
        Intent intent = new Intent(getContext(), Chatroom.class);
        intent.putExtra(Chatroom.TALKER_ID, post.getSellerID());
        startActivity(intent);
    }

    private void deletePostFromDB() {
        mDatabase.child(post.getPostID()).removeValue();
        Toast.makeText(getContext(), "Post deleted successfully!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        if (mUsername == post.getSellerID()) {
            deletePost.setVisibility(View.VISIBLE);
            contactSeller.setVisibility(View.GONE);
        }
        else {
            deletePost.setVisibility(View.GONE);
            contactSeller.setVisibility(View.VISIBLE);
        }
    }

}