package com.example.marketplace;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.URL;

public class Profile extends Fragment {

    private ImageView imgUser;
    private TextView txtDisplayName;
    private TextView txtEmail;
    private Button btnPreferences;
    private Button btnCreate;
    private Button btnDelete;
    private TextView txtUni;
    private FirebaseUser mFirebaseUser;

    public Profile() {

    }

    public interface ProfileListener {
        public void createPost();
        public void openPreferences();
    }

    ProfileListener PL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        PL = (ProfileListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        imgUser = view.findViewById(R.id.imgUser);
        txtDisplayName = view.findViewById(R.id.txtDisplayName);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnPreferences = view.findViewById(R.id.btnPreferences);
        btnCreate = view.findViewById(R.id.btnCreate);
        btnDelete = view.findViewById(R.id.btnDelete);
        txtUni = view.findViewById(R.id.txtUni);

        mFirebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            txtDisplayName.setText(mFirebaseUser.getDisplayName());
            txtEmail.setText(mFirebaseUser.getEmail());
            try {
                URL url = new URL(mFirebaseUser.getPhotoUrl().toString());
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Drawable d = new BitmapDrawable(getResources(), image);
                imgUser.setImageDrawable(d);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Could not load the Image", Toast.LENGTH_SHORT).show();
            }
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PL.createPost();
            }
        });

        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PL.openPreferences();
            }
        });


        return view;
    }
}
