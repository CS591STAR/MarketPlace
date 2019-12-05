package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements NavBarFragment.NavBarFragmentListener, MarketFeed.MarketFeedListener, ItemPostForm.ItemPostFormListener {

    User you;
    MarketFeed marketFeed;
    Profile profile;
    Chatroom chats;
    ItemPostForm itemPostForm;
    ViewPost viewPost;
    // add search also
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
    public void search() {

    }


    @Override
    public void openFeed() {

        if (marketFeed == null) {
            marketFeed = new MarketFeed();
        }
        marketFeed = (MarketFeed) fm.findFragmentByTag("Market Feed");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragLayout, marketFeed);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void returnToFeed() {
        openFeed();
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

//    @Override
//    public void selectedPost(Post post) {
//
//        if (viewPost == null) {
//            viewPost = new ViewPost(post);
//        }
//
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragLayout, viewPost, "ViewPost");
//        ft.addToBackStack(null);
//        ft.commit();
//    }

}
