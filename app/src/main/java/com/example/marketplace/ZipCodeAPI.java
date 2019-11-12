package com.example.marketplace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ZipCodeAPI  extends AppCompatActivity {

    private EditText zipcode1;
    private EditText zipcode2;
    private Button checkDisBtn;
    private TextView resultTxt;
    private String finaldistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zipcodeapi);

        zipcode1 = findViewById(R.id.zipcode1);
        zipcode2 = findViewById(R.id.zipcode2);
        checkDisBtn = findViewById(R.id.checkDisBtn);
        resultTxt = findViewById(R.id.resultTxt);

        checkDisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getDistance();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // The following method is used to make the API call from the zipcode api

    public void getDistance() throws IOException {
//        String dist1 = zipcode1.getText().toString();
//        String dist2 = zipcode2.getText().toString();
//        String locations = dist1 + "/" + dist2 + "/mi";

        OkHttpClient client = new OkHttpClient();
        //String url = "http://www.zipcodeapi.com/rest/"+ gitignore.zipcodeAPIKey + "/distance.json/" + locations;
        String url = "http://www.zipcodeapi.com/rest/N9iqyW84hPLnlCht5BZ94X03OuuHOMAgyAD447YDjgtjAAStwdVErHd8lRiac46Q/distance.json/02128/02114/mile";


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String distance = response.body().string();

                    ZipCodeAPI.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultTxt.setText(distance);
                        }
                    });
                }
            }
        });
    }
}
