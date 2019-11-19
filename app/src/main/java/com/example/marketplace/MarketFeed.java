package com.example.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MarketFeed extends AppCompatActivity {
    Button testBtn;
    private ListView feedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        NavBarFragment topFrag = new NavBarFragment();
//        fragmentTransaction.add(R.id.topFrag, topFrag);
//        fragmentTransaction.commit();
        feedListView = findViewById(R.id.feedListView);
        testBtn = findViewById(R.id.testBtn);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemPostForm.class);
                startActivity(intent);
            }
        });

    }
}
