package com.example.marketplace;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EBayAPI {

    private boolean isTokenValid = false;
    private String token;
    private JSONObject response;

    private String str = "EBayAPI";

    private static EBayAPI instance;

    private EBayAPI(){}

    public static EBayAPI getInstance(){
        if(instance == null){
            instance = new EBayAPI();
        }
        return instance;
    }

    public JSONObject getResponse() {
        return response;
    }

    private void setResponse(JSONObject res){
        response = res;
    }

    /**
     * programmatically get the token for later API request
     */
    private void getToken() {
        //new okHttp object
        OkHttpClient client = new OkHttpClient();

        //create request body
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=https%3A%2F%2Fapi.ebay.com%2Foauth%2Fapi_scope");
        Request request = new Request.Builder()
                .url("https://api.ebay.com/identity/v1/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + gitignore.eBayAuth)
                .build();

        //try to make a call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    //set the token
                    try {
                        token = new JSONObject(response.body().string()).getString("access_token");
                        isTokenValid = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void searchItem(String keyword, int limit){
        if(!isTokenValid){
            getToken();
        }

        //new okHttp object
        OkHttpClient client = new OkHttpClient();

        //create request body
        Request request = new Request.Builder()
                .url("https://api.ebay.com/buy/browse/v1/item_summary/search?q=" + keyword + "&limit="+ limit)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        //try to make a call to the API
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //if call fails, print out message
                e.printStackTrace();
                isTokenValid = false;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if call succeeds, get item information from response body, and set text on screen
                if(response.isSuccessful()){
                    try {
                        String responseBody = response.body().string();
                        JSONObject object = new JSONObject(responseBody);
                        setResponse(object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    isTokenValid = false;
                }
            }
        });
    }

    public void searchItem(String keyword){
        searchItem(keyword, 1);
    }
}
