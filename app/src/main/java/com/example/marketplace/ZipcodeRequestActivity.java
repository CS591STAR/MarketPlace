package com.example.marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ZipcodeRequestActivity extends AppCompatActivity {

    private EditText etZip;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_request);

        etZip = (EditText) findViewById(R.id.etZip);
        btnApply = (Button) findViewById(R.id.btnApply);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = etZip.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("zip").setValue(zip);
                onBackPressed();
            }
        });
    }
}
