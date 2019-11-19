package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements NavBarFragment.NavBarFragmentListener {

    User you;
    MarketFeed marketFeed;
    Profile profile;
    Chatroom chats;
    // add search also
    LinearLayout fragLayout;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle data = getIntent().getExtras();
        you = data.getParcelable("user");

        marketFeed = new MarketFeed();
        profile = new Profile();
        chats = new Chatroom();

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
}
