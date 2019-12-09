package com.example.marketplace;


import android.content.Context;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Profile extends Fragment {

    private ImageView imgUser;
    private TextView txtDisplayName;
    private TextView txtEmail;
    private TextView txtUni;
    private Button btnPreferences;
    private TextView txtRating;
    private RatingBar rbUser;
    private TextView txtStars;
    private TextView txtPosts;

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;

    private DatabaseReference mDatabase;
    private FirebaseUser mFirebaseUser;


    public Profile() {

    }

    public interface ProfileListener {
        public void openPreferences();
    }

    ProfileListener PL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        PL = (ProfileListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        // init the adapter
        postListAdapter = new PostListAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postListAdapter); // set the adapter to the recycler view


        imgUser = view.findViewById(R.id.imgUser);
        txtDisplayName = view.findViewById(R.id.txtDisplayName);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnPreferences = view.findViewById(R.id.btnPreferences);
        txtUni = view.findViewById(R.id.txtUni);
        txtRating = view.findViewById(R.id.txtRating);
        txtStars = view.findViewById(R.id.txtStars);
        rbUser = view.findViewById(R.id.rbUser);
        rbUser.setNumStars(5);

        txtPosts = view.findViewById(R.id.txtPosts);

        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            txtDisplayName.setText(mFirebaseUser.getDisplayName());
            txtEmail.setText(mFirebaseUser.getEmail());

            Uri img = mFirebaseUser.getPhotoUrl();
            GlideApp.with(this /* context */)
                    .load(img)
                    .into(imgUser);
        }

        rbUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                //save the rating to the database

                Toast.makeText(getContext(), "Thank you for your feedback!", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rbUser.setRating(0);
            }
        });

        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PL.openPreferences();
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference(); // get the ref of db

        mDatabase.child("posts").orderByChild("sellerID").equalTo(mFirebaseUser.getUid()).addValueEventListener( new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                postList.clear();
                if(dataSnapshot.hasChildren()){
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()){
                        DataSnapshot snap = iter.next();
                        String postID = snap.getKey();
                        try {
                            long askingPrice = (long) snap.child("askingPrice").getValue();
                            String category = (String) snap.child("category").getValue();
                            String itemDescription = (String) snap.child("itemDescription").getValue();
                            String itemName = (String) snap.child("itemName").getValue();

                            long itemPostTime = (long) snap.child("itemPostTime").getValue();

                            String sellerID = (String) snap.child("sellerID").getValue();
                            String zipcode = (String) snap.child("zipcode").getValue();
                            String itemCondition = (String) snap.child("itemCondition").getValue();
                            String image = (String) snap.child("image").getValue();

                            Post post = new Post(itemName, askingPrice, zipcode, sellerID, category, itemCondition,
                                    itemPostTime, itemDescription, postID, image);
                            postList.add(post);
                            //received results
                            Log.i("post", post.getItemName() + " on nod " + postID);
                        }
                        catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // notify the adapter
                Collections.reverse(postList);
                postListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });


        return view;
    }
}
