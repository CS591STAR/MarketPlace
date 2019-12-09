package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity implements NavBarFragment.NavBarFragmentListener, MarketFeed.MarketFeedListener, ItemPostForm.ItemPostFormListener, Profile.ProfileListener, ViewPost.ViewPostListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        Bundle data = getIntent().getExtras();
        you = data.getParcelable("user");

        marketFeed = new MarketFeed();
        profile = new Profile();
        chats = new Chatroom();
        itemPostForm = new ItemPostForm();
        preferences = new Preferences();

        //set eBay API ready
        EBayAPI.getInstance().getToken();

        fragLayout = findViewById(R.id.fragLayout);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragLayout, marketFeed, "Market Feed");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openProfile() {

        if (profile == null) {
            profile = new Profile();
        }
        FragmentTransaction ft = fm.beginTransaction();
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
