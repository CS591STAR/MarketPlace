package com.example.marketplace;


import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OtherProfile extends Fragment {

    private ImageView otherImgUser;
    private TextView txtDisplayName;
    private TextView txtEmail;
    private TextView txtUni;
    private TextView txtRating;
    private RatingBar rbUser;
    private TextView txtStars;
    private TextView txtPosts;
    String ratingString;
    long numRatings;
    double ratingVal;
    String ratingValString;
    String userPhoto;

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;

    private DatabaseReference mDatabase;
    private String otherUser;

    public OtherProfile() {

    }

    public interface otherProfileListener {
        public void openPreferences();
    }

    otherProfileListener OPL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OPL = (otherProfileListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.other_user_layout, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            otherUser = bundle.getString("user", "");
        }


        recyclerView = view.findViewById(R.id.recyclerView);
        // init the adapter
        postListAdapter = new PostListAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postListAdapter); // set the adapter to the recycler view


        otherImgUser = view.findViewById(R.id.otherImgUser);
        txtDisplayName = view.findViewById(R.id.txtDisplayName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtUni = view.findViewById(R.id.txtUni);
        txtRating = view.findViewById(R.id.txtRating);
        txtStars = view.findViewById(R.id.txtStars);
        rbUser = view.findViewById(R.id.rbUser);
        rbUser.setMax(5);

        txtPosts = view.findViewById(R.id.txtPosts);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // get the ref of db

        mDatabase.child("users").child(otherUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                txtDisplayName.setText(dataSnapshot.child("name").getValue().toString());
                txtEmail.setText(dataSnapshot.child("email").getValue().toString());
                txtUni.setText(dataSnapshot.child("uni").getValue().toString());

                ratingValString = String.valueOf(dataSnapshot.child("rating").getValue());
                ratingVal = Double.parseDouble(ratingValString);
                numRatings = (long) (dataSnapshot.child("numRatings").getValue());

                BigDecimal bd = new BigDecimal(ratingVal).setScale(2, RoundingMode.HALF_UP);
                double newInput = bd.doubleValue();

                String overall = getResources().getString(R.string.overall_rating);
                ratingString = overall + " " + newInput + " " + getString(R.string.stars);

                userPhoto = dataSnapshot.child("img").getValue().toString();
                txtStars.setText(ratingString);


                if (userPhoto != null) {

                    GlideApp.with(getContext() /* context */)
                            .load(userPhoto)
                            .into(otherImgUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        rbUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b) {
                    double oldTotal = ratingVal * numRatings;
                    double newTotal = oldTotal + v;
                    double newNumRatings = numRatings + 1;
                    double newRating = newTotal / (numRatings + 1);

                    mDatabase.child("users").child(otherUser).child("rating").setValue(newRating);
                    mDatabase.child("users").child(otherUser).child("numRatings").setValue(newNumRatings);

                    Toast.makeText(getContext(), "Thank you for your feedback!", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mDatabase.child("posts").orderByChild("sellerID").equalTo(otherUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postList.clear();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
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
                            String eBayPrice = (String) snap.child("eBayPrice").getValue();
                            String amazonPrice = (String) snap.child("amazonPrice").getValue();

                            Post post = new Post(itemName, askingPrice, zipcode, sellerID, category, itemCondition,
                                    itemPostTime, itemDescription, postID, image, eBayPrice, amazonPrice);
                            postList.add(post);
                            //received results
                            Log.i("post", post.getItemName() + " on nod " + postID);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // notify the adapter
                Collections.reverse(postList);
                postListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }
}
