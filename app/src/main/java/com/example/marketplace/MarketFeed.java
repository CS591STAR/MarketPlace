package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MarketFeed extends AppCompatActivity {

    private User you;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);

        Bundle data = getIntent().getExtras();
        you = data.getParcelable("user");

        Toast.makeText(getApplicationContext(), you.getName(), Toast.LENGTH_SHORT).show();


        //TODO send user information to the fragment!!!!!
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("user", you);
//        NavBarFragment fragment = new NavBarFragment();
//        fragment.setArguments(bundle);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        NavBarFragment topFrag = new NavBarFragment();
//        fragmentTransaction.add(R.id.topFrag, topFrag);
//
//        topFrag.setArguments(bundle);
//
//        fragmentTransaction.commit();

    }
}
