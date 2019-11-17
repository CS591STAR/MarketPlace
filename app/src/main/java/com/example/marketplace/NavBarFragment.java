package com.example.marketplace;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class NavBarFragment extends Fragment {

    Button btnProfile;
    Button btnChat;
    EditText txtSearch;
    Button btnSearch;
    Button btnSell;

    public NavBarFragment(){
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_bar, container, false);

        btnProfile = view.findViewById(R.id.btnProfile);
        btnChat = view.findViewById(R.id.btnChat);
        txtSearch = view.findViewById(R.id.txtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSell = view.findViewById(R.id.btnSell);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
