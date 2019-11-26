package com.example.marketplace;


import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class MarketFeed extends Fragment {

    Button btnCreatePost;
    ListView feedListView;
    SharedPreferences sharedPref;

    public MarketFeed() {
        // Required empty public constructor
    }

    public interface MarketFeedListener {
        public void createPost();
    }

    MarketFeedListener MFL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MFL = (MarketFeedListener) context;
    }

    User you;
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feed_layout, container, false);

        feedListView = view.findViewById(R.id.feedListView);
        btnCreatePost = view.findViewById(R.id.btnCreatePost);

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFL.createPost();
            }
        });

        return view;
    }


//    @Override
//    protected void onDestroy() {
//
//        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        Gson gson = new Gson();
//        String youJson = gson.toJson(you);
//        editor.putString("user", youJson);
//        editor.commit();
//        super.onDestroy();
//    }
}
