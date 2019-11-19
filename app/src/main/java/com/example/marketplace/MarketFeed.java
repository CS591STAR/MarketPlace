package com.example.marketplace;


import android.content.Context;
import android.content.SharedPreferences;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;


public class MarketFeed extends AppCompatActivity {
    Button testBtn;
    private ListView feedListView;

    User you;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);


        Bundle data = getIntent().getExtras();
        you = data.getParcelable("user");

        // Toast.makeText(getApplicationContext(), you.getName(), Toast.LENGTH_SHORT).show();


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
