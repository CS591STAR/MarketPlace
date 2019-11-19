package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    ImageView imgUser;
    TextView txtDisplayName;
    TextView txtEmail;
    Button btnEdit;
    Button btnPosts;
    Button btnChats;
    TextView txtUni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        NavBarFragment topFrag = new NavBarFragment();
//        fragmentTransaction.add(R.id.topFrag, topFrag);
//        fragmentTransaction.commit();

        imgUser = findViewById(R.id.imgUser);
        txtDisplayName = findViewById(R.id.txtDisplayName);
        txtEmail = findViewById(R.id.txtEmail);
        btnEdit = findViewById(R.id.btnEdit);
        btnPosts = findViewById(R.id.btnPosts);
        btnChats = findViewById(R.id.btnChats);
        txtUni = findViewById(R.id.txtUni);

        //add chat button listener
        btnChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), Chatroom.class));
            }
        });

    }
}
