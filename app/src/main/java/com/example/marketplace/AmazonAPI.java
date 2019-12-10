package com.example.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AmazonAPI {

    private static AmazonAPI instance;
    private JSONObject response;
    private static String amazonPrice;
    private String TAG = "amznAPI";

    private AmazonAPI() {
    }

    public static AmazonAPI getInstance() {
        if (instance == null) {
            instance = new AmazonAPI();
        }
        return instance;
    }

    public static String getAmazonPrice() {
        return amazonPrice;
    }

    public static void setAmazonPrice(String eBayPrice) {
        AmazonAPI.amazonPrice = amazonPrice;
    }

    public JSONObject getResponse() {
        return response;
    }

    private void setResponse(JSONObject res) {
        response = res;
    }

    public void getAmazonPrice(final String keyword, final DatabaseReference db) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://amazon-price1.p.rapidapi.com/search?keywords=" + keyword + "&marketplace=US";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "amazon-price1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", gitignore.amazon_api_key)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        final String price = (String) jsonArray.getJSONObject(0).get("price");
                        amazonPrice = price;
                        Log.w(TAG, "price: " + price);
                        db.setValue(price);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("error: ", "error here!" );
                }
            }
        });

    }

    public void searchItem (String keyword, DatabaseReference db){
        Log.w(TAG, "keyword: " + keyword);
        getAmazonPrice(keyword, db);
        }

}

