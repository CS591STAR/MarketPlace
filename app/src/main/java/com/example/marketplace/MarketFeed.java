package com.example.marketplace;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarketFeed extends AppCompatActivity {

    private Button btnTest;
    private TextView txtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout);

        btnTest = findViewById(R.id.btnTest);
        txtTest = findViewById(R.id.txtTest);
    }
}
