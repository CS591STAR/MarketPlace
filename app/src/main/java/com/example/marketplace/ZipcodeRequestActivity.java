package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.MoreObjects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ZipcodeRequestActivity extends AppCompatActivity {

    private EditText etZip;
    private Button btnApply;
    private EditText etUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_request);

        etZip = (EditText) findViewById(R.id.etZip);
        btnApply = (Button) findViewById(R.id.btnApply);
        etUniversity = (EditText) findViewById(R.id.etUniversity);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = etZip.getText().toString();
                String university = etUniversity.getText().toString();
                while(zip == null || university == null){
                    Toast.makeText(getBaseContext() ,getResources().getString(R.string.please_fill_in_zip), Toast.LENGTH_SHORT).show();
                    zip = etZip.getText().toString();
                    university = etUniversity.getText().toString();
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child("zip").setValue(zip);
                reference.child("uni").setValue(university);
                onBackPressed();
            }
        });
    }
}
