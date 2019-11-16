package com.example.marketplace;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        // For the purpose of testing the api get call, we are going to add
        // a simple layout that allows users to enter two random zip codes and when they click
        // on the button that says 'Check Distance` the output of the fetch call will be placed
        // in the 'Result' text view.

        zipcode1 = findViewById(R.id.zipcode1);
        zipcode2 = findViewById(R.id.zipcode2);
        checkDisBtn = findViewById(R.id.checkDisBtn);
        resultTxt = findViewById(R.id.resultTxt);

        // Call the getDistance() method that makes the get call to the api
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

    // The following method is used to make the API call

    public void getDistance() throws IOException {

        // The following concatenates the zip codes that the user entered
        // with the rest of the link needed to make the get call

        String dist1 = zipcode1.getText().toString();
        String dist2 = zipcode2.getText().toString();
        String locations = dist1 + "/" + dist2 + "/mile";

        // We use OkHttpClient to make the get call with the headers needed as stated in the
        // api documentation

        OkHttpClient client = new OkHttpClient();
        String url = "https://redline-redline-zipcode.p.rapidapi.com/rest/distance.json/" + locations;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "redline-redline-zipcode.p.rapidapi.com")
                .addHeader("x-rapidapi-key",gitignore.zipcodeAPIKey)
                .build();

        // the following methods were required by the newCall and callBack interfaces
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            // Here we set the 'Response' text view to the get call response
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
