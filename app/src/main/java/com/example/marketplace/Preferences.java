package com.example.marketplace;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Preferences extends Fragment {

    private TextView txt1;
    private EditText edit1;
    private Button btn1;
    private TextView txt3;
    private EditText edit3;
    private Button btn3;
    private TextView txt4;
    private EditText edit4;
    private Button btn4;
    private TextView txt5;
    private EditText edit5;
    private Button btn5;


    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    public Preferences() {

    }

//    public interface PreferencesListener {
//
//    }
//
//    PreferencesListener PL;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        PL = (PreferencesListener) context;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pref_layout, container, false);

        txt1 = view.findViewById(R.id.txt1);
        edit1 = view.findViewById(R.id.edit1);
        btn1 = view.findViewById(R.id.btn1);
        txt3 = view.findViewById(R.id.txt3);
        edit3 = view.findViewById(R.id.edit3);
        btn3 = view.findViewById(R.id.btn3);
        txt4 = view.findViewById(R.id.txt4);
        edit4 = view.findViewById(R.id.edit4);
        btn4 = view.findViewById(R.id.btn4);
        txt5 = view.findViewById(R.id.txt5);
        edit5 = view.findViewById(R.id.edit5);
        btn5 = view.findViewById(R.id.btn5);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(mFirebaseUser.getUid()).child("name").setValue(edit1.getText().toString());
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(mFirebaseUser.getUid()).child("uni").setValue(edit3.getText().toString());
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(mFirebaseUser.getUid()).child("zip").setValue(edit4.getText().toString());
            }
        });

//        btn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return view;
    }

}
