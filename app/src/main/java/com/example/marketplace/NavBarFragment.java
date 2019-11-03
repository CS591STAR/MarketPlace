package com.example.marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NavBarFragment extends Fragment {

    private Button btnProfile;
    private Button btnChat;
    private TextView txtSearch;
    private Button btnSearch;
    private Button btnSell;

    public NavBarFragment(){
        //Required empty public constructor
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


        return view;
    }
}
