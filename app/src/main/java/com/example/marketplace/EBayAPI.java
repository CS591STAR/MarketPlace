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
    public static String eBayPrice;

    private static final String TAG = "EBayAPI";

    private static EBayAPI instance;

    public static String geteBayPrice() {
        return eBayPrice;
    }

    public static void seteBayPrice(String eBayPrice) {
        EBayAPI.eBayPrice = eBayPrice;
    }

    private EBayAPI() {
    }

    public static EBayAPI getInstance() {
        if (instance == null) {
            instance = new EBayAPI();
        }
        return instance;
    }

    public JSONObject getResponse() {
        return response;
    }

    private void setResponse(JSONObject res) {
        response = res;
    }

    /**
     * programmatically get the token for later API request
     */
    public void getToken() {
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
                if (response.isSuccessful()) {
                    //set the token
                    try {
                        token = new JSONObject(response.body().string()).getString("access_token");

                        //after getting a valid token, set the boolean
                        isTokenValid = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //search items by keyword
    public void searchItemAndFillIn(final String keyword, int limit) {
        if (!isTokenValid) {
            getToken();
        }

        //new okHttp object
        OkHttpClient client = new OkHttpClient();

        //create request body
        Request request = new Request.Builder()
                .url("https://api.ebay.com/buy/browse/v1/item_summary/search?q=" + keyword + "&limit=" + limit)
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
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject object = new JSONObject(responseBody);
                        //get the list of items
                        JSONArray array = object.getJSONArray("itemSummaries");
                        //get only the 1st item (which should be the most relevant)
                        object = array.getJSONObject(0);
                        //get the image
                        String url = object.getJSONObject("image").getString("imageUrl");
                        //get the price
                        JSONObject priceObject = object.getJSONObject("price");
                        final String price = "$" + priceObject.getString("value");
                        Log.w(TAG, "keyword: " + keyword);
                        Log.w(TAG, "price: " + price);
                        eBayPrice = price;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isTokenValid = false;
                }
            }
        });
    }

    public void searchItem(String keyword) {
        searchItemAndFillIn(keyword, 1);
    }

    public String getItemPrice() throws JSONException {
        if (response == null) {
            return "No result";
        }

        return response.getString("value") + " " + response.getString("currency");
    }
}
