package com.example.ebayapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class EbayAPI extends AppCompatActivity {

    Button bSearch;
    TextView tvItem;
    EditText etKeyword;

    String str = "MA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebay_api);

        bSearch = (Button) findViewById(R.id.bSearch);
        tvItem = (TextView) findViewById(R.id.tvItem);
        etKeyword = (EditText) findViewById(R.id.etKeyword);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchItem();
            }
        });
    }

    /**
     * Send the keyword to Ebay's Browsing API to get the information about names, prices, etc
     */
    private void searchItem() {
        //get keyword from text box
        String keyword = etKeyword.getText().toString();

        //new okHttp object
        OkHttpClient client = new OkHttpClient();

        //create request body
        Request request = new Request.Builder()
                .url("https://api.ebay.com/buy/browse/v1/item_summary/search?q="+keyword+"&limit=1")
                .get()
                .addHeader("Authorization", "Bearer Token")
                .addHeader("Host", "api.ebay.com")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Cookie", "ebay=%5Esbf%3D%23%5E")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        //try to make a call to the API
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //if call fails, print out message
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if call succeeds, get item information from response body, and set text on screen
                if(response.isSuccessful()){
                    String title = "";
                    String price = "";
                    String responseBody = "";
                    try {
                        //To fetch only the needed info from response body

                        //cast response body into a JsonObject
                        responseBody = response.body().string();
                        JSONObject object = new JSONObject(responseBody);
                        //get the list of items
                        JSONArray array = object.getJSONArray("itemSummaries");
                        //get only the 1st item (which should be the most relevant)
                        object = array.getJSONObject(0);
                        //get the title
                        title = object.getString("title");
                        //get the image
                        String url = object.getJSONObject("image").getString("imageUrl");
                        //get the price
                        JSONObject priceObject = object.getJSONObject("price");
                        price = priceObject.getString("value")+" "+priceObject.getString("currency");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //put strings together
                    final String info = responseBody;
                    final String res = responseBody;

                    EbayAPI.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvItem.setText(info);
                            Log.w("MA",res);
                        }
                    });
                }
            }
        });
    }
}
