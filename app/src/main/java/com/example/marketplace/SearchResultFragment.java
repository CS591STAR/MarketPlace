package com.example.marketplace;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {

    private String keyword;
    private RecyclerView resultRecyclerView;
    private List<Post> postList = new ArrayList<>();
    private PostListAdapter postListAdapter;

    private DatabaseReference mDatabase;
    private Query query;

    private static final String TAG = "SEARCH";

    public SearchResultFragment() {
        // Required empty public constructor
    }

    public SearchResultFragment(String keyword, Query query) {
        this.keyword = keyword;
        this.query = query;
        Log.w(TAG, "The keyword is " + keyword);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        resultRecyclerView = (RecyclerView) view.findViewById(R.id.resultRecyclerView);
        // init the adapter
        postListAdapter = new PostListAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        resultRecyclerView.setLayoutManager(mLayoutManager);
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        resultRecyclerView.setItemAnimator(new DefaultItemAnimator());
        resultRecyclerView.setAdapter(postListAdapter); // set the adapter to the recycler view

        mDatabase = FirebaseDatabase.getInstance().getReference(); // get the ref of db

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i("dataInside", dataSnapshot.getValue().toString());
                }

                postList.clear();
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
                        DataSnapshot snap = iter.next();
                        String postID = snap.getKey();
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
                        if (post.getItemName().toLowerCase().indexOf(keyword.toLowerCase()) >= 0) {
                            Log.w(TAG, String.valueOf(post.getItemName().indexOf(keyword)));
                            postList.add(post);
                        }
                        //received results
                        Log.i("post", post.getItemName() + " on nod " + postID);
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
