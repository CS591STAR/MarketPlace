package com.example.marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity implements NavBarFragment.NavBarFragmentListener, MarketFeed.MarketFeedListener, ItemPostForm.ItemPostFormListener, Profile.ProfileListener, ViewPost.ViewPostListener, OtherProfile.otherProfileListener {

    User you;
    MarketFeed marketFeed;
    Profile profile;
    Chatroom chats;
    ItemPostForm itemPostForm;
    Preferences preferences;
    ViewPost viewPost;
    LinearLayout fragLayout;
    FragmentManager fm;
    Post post;
    OtherProfile otherProfile;

    NavBarFragment navBar;
    String userID;
    FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);


        marketFeed = new MarketFeed();
        profile = new Profile();
        chats = new Chatroom();
        itemPostForm = new ItemPostForm();
        preferences = new Preferences();
        otherProfile = new OtherProfile();

        //set eBay API ready
        EBayAPI.getInstance().getToken();

        fragLayout = findViewById(R.id.fragLayout);
        fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        navBar = (NavBarFragment) fm.findFragmentById(R.id.navBar);
//        ft.hide(navBar);
        ft.add(R.id.fragLayout, marketFeed, "Market Feed");
        ft.addToBackStack(null);
        ft.commit();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        userID = mFirebaseUser.getUid();


        mDatabase.orderByKey().equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    User you = new User(mFirebaseUser.getUid(), mFirebaseUser.getDisplayName(), mFirebaseUser.getEmail(), mFirebaseUser.getPhotoUrl().toString(), "", 5, 1, 0);
                    mDatabase.child(userID).setValue(you);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void openProfile() {

        if (profile == null) {
            profile = new Profile();
        }
        FragmentTransaction ft = fm.beginTransaction();
//        ft.hide(navBar);

        ft.replace(R.id.fragLayout, profile, "Profile");
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void openChats() {
        Intent intent = new Intent(getApplicationContext(), ChatList.class);
        startActivity(intent);
    }

    @Override
    public void search(String keyword) {
        searchByKeyword(keyword);
    }

    public void searchByKeyword(String keyword){
        Query query = null;
        if(marketFeed != null){
            query = marketFeed.getCurrentQuery();
        }
        SearchResultFragment resultFragment = new SearchResultFragment(keyword, query);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragLayout, resultFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openFeed() {
        marketFeed = (MarketFeed) fm.findFragmentByTag("Market Feed");
        if (marketFeed == null) {
            marketFeed = new MarketFeed();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragLayout, marketFeed);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void openOtherProfile(String sellerID) {

        if (otherProfile == null) {
            otherProfile = new OtherProfile();
        }

        Bundle bundle = new Bundle();
        bundle.putString("user", sellerID);
        otherProfile.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragLayout, otherProfile, "OtherProfile");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void createPost() {

        if (itemPostForm == null) {
            itemPostForm = new ItemPostForm();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragLayout, itemPostForm, "ItemPostForm");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openPreferences() {

        if (preferences == null) {
            preferences = new Preferences();
        }


        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.fragLayout, preferences, "Preferences");
        ft.addToBackStack(null);
        ft.commit();
    }

}
