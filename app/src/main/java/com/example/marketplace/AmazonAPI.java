package com.example.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

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
    private String amazonPrice;

    private AmazonAPI() {
    }

    public static AmazonAPI getInstance() {
        if (instance == null) {
            instance = new AmazonAPI();
        }
        return instance;
    }

    public JSONObject getResponse() {
        return response;
    }

    private void setResponse(JSONObject res) {
        response = res;
    }

    public void getAmazonPrice(final FragmentActivity activity, final String keyword) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://amazon-price1.p.rapidapi.com/search?keywords=" + "macbook" + "&marketplace=US";

        Request request = new Request.Builder()
                .url("https://amazon-price1.p.rapidapi.com/search?keywords=macbook&marketplace=US")
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
                String responseBody = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    amazonPrice = (String) jsonArray.getJSONObject(0).get("price");
                    Bundle sendPrice = new Bundle();
                    sendPrice.putString("amazonPrice", amazonPrice);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //        public void searchItem (FragmentActivity activity, String keyword){
//            getAmazonPrice(activity, keyword, 1);
//        }
}

