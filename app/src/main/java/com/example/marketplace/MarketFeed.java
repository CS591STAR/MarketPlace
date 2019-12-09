package com.example.marketplace;


import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MarketFeed extends Fragment {

    Button btnCreatePost;
    ListView feedListView;
    SeekBar distanceSeekBar;
    Button filterByDistanceBtn;
    SharedPreferences sharedPref;
    String mileRadius;

    private Button btnSortByPrice;
    private Spinner sprCategory;

    private DatabaseReference mDatabase;
    private DatabaseReference zipcodeDatabase;

    ArrayList<String> zipCodesInRadius = new ArrayList<>();
    ArrayList<String> zipcodesToCompare = new ArrayList<>();

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private ValueEventListener basicValueEventListener;
    private Query currentQuery;
    private Query queryByPostTime;
    private Query queryByPrice;

    private static final String TAG = "FEED";

    public MarketFeed() {
        // Required empty public constructor
    }

    public interface MarketFeedListener {
        public void createPost();
    }

    MarketFeedListener MFL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MFL = (MarketFeedListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.feed_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.feed_recycler_view);

        btnCreatePost = view.findViewById(R.id.btnCreatePost);
        // init the adapter
        postListAdapter = new PostListAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postListAdapter); // set the adapter to the recycler view
        filterByDistanceBtn = view.findViewById(R.id.filterByDistanceBtn);
        distanceSeekBar = view.findViewById(R.id.distanceSeekBar);

        //add button for sorting by price
        btnSortByPrice = view.findViewById(R.id.btnSortByPrice);
        btnSortByPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByPrice();
            }
        });

        // give the seek bar a max value of 5 miles
        distanceSeekBar.setMax(5);

        filterByDistanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceSeekBar.setVisibility(View.VISIBLE);
            }
        });

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mileRadius = String.valueOf(distanceSeekBar.getProgress());
                try {
                    zipcodesInRadius();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //set dropdown menu of categories
        sprCategory = view.findViewById(R.id.sprCategory);
        sprCategory.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories_including_all_categories_option)));
        sprCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentQuery != null) {
                    currentQuery.removeEventListener(basicValueEventListener);
                }
                if (i == 0) {
                    currentQuery = mDatabase.child("posts").orderByChild("category");
                    currentQuery.addValueEventListener(basicValueEventListener);
                } else {
                    currentQuery = mDatabase.child("posts").orderByChild("category").equalTo(Post.Category.values()[i - 1].toString());
                    currentQuery.addValueEventListener(basicValueEventListener);
//                    for(int index = 0; index < postList.size(); index++){
//                        String currentCategory = postList.get(index).getCategory();
//                        String neededCategory = Post.Category.values()[i-1].toString();
//                        Log.w(TAG, "Category: " + currentCategory + " " + neededCategory);
//                        if(!currentCategory.equals(neededCategory)) {
//                            postList.remove(index);
//                        }
//                    }
//                    postListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFL.createPost();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference(); // get the ref of db

        if (basicValueEventListener == null) {
            basicValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    postList.clear();

                    if (dataSnapshot.hasChildren()) {
                        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                        while (iter.hasNext()) {
                            DataSnapshot snap = iter.next();
                            String postID = snap.getKey();
                            try {
                                long askingPrice = (long) snap.child("askingPrice").getValue();
                                String category = (String) snap.child("category").getValue();
                                String itemDescription = (String) snap.child("itemDescription").getValue();
                                String itemName = (String) snap.child("itemName").getValue();

                                long itemPostTime = (long) snap.child("itemPostTime").getValue();

                                String sellerID = (String) snap.child("sellerID").getValue();
                                String zipcode = (String) snap.child("zipcode").getValue();
                                String itemCondition = (String) snap.child("itemCondition").getValue();
                                String image = (String) snap.child("image").getValue();

                                Post post = new Post(itemName, askingPrice, zipcode, sellerID, category, itemCondition,
                                        itemPostTime, itemDescription, postID, image);
                                postList.add(post);
                                //received results
                                Log.i("post", post.getItemName() + " on nod " + postID);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // notify the adapter
                    Collections.reverse(postList);
                    postListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

                ;
            };
        }
        if (currentQuery != null) {
            currentQuery.removeEventListener(basicValueEventListener);
        }
        currentQuery = mDatabase.child("posts").orderByChild("itemPostTime");
        currentQuery.addValueEventListener(basicValueEventListener);

        return view;
    }

    private void sortByPrice() {
        currentQuery.removeEventListener(basicValueEventListener);
        currentQuery = mDatabase.child("posts").orderByChild("askingPrice");
        currentQuery.addValueEventListener(basicValueEventListener);
    }

    private void sortByPostTime() {
        currentQuery.removeEventListener(basicValueEventListener);
        currentQuery = mDatabase.child("posts").orderByChild("itemPostTime");
        currentQuery.addValueEventListener(basicValueEventListener);
    }

    private void filterByCategory(int category) {
        ;
    }

//    @Override
//    protected void onDestroy() {
//
//        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        Gson gson = new Gson();
//        String youJson = gson.toJson(you);
//        editor.putString("user", youJson);
//        editor.commit();
//        super.onDestroy();
//    }

    public void zipcodesInRadius() throws IOException {
        Log.i("SORT", "sort by distance");

        // clear lists
        zipCodesInRadius.clear();
        zipcodesToCompare.clear();

        // for now we are using a sample zipcode until we retrieve it from the user properly
        String sampleZip = "02128";
        String redLineAPIEndPoint = "https://redline-redline-zipcode.p.rapidapi.com/rest/radius.json/" + sampleZip + "/" + mileRadius + "/mile";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(redLineAPIEndPoint)
                .get()
                .addHeader("x-rapidapi-host", "redline-redline-zipcode.p.rapidapi.com")
                .addHeader("x-rapidapi-key", gitignore.zipcodeAPIKey)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("redline api fetch: ", "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("API", "call to api success");

                    try {
                        JSONArray fetchResponse = new JSONObject(response.body().string()).getJSONArray("zip_codes");
                        for (int obj = 0; obj < fetchResponse.length(); obj++) {
                            JSONObject zipcode_inresponse = fetchResponse.getJSONObject(obj);
                            zipCodesInRadius.add(zipcode_inresponse.getString("zip_code"));
                        }

                        sortByDistance();

//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                postListAdapter.notifyDataSetChanged();
//                            }
//                        });
                    } catch (JSONException e) {
                        Log.i("onresponse", "not successful");
                    }

                } else {
                    Log.i("zipcode api fetch ", "did not work");
                }
            }
        });
    }

    public void sortByDistance() {

        postList.clear();
        currentQuery.removeEventListener(basicValueEventListener);
        mDatabase.child("zipcodes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, ArrayList<String>> zipcodes = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentZipCode = snapshot.getKey(); // Key for hashmap
                    Log.i("CURR", "current zipcode is " + currentZipCode);
                    zipcodesToCompare.add(currentZipCode);
                    ArrayList<String> postsInZipcode = new ArrayList<>();
                    for (DataSnapshot zipcodeChildren : snapshot.getChildren()) {
                        postsInZipcode.add(zipcodeChildren.getKey());
                    }
                    Log.i("inside", postsInZipcode.toString());
                    zipcodes.put(currentZipCode, postsInZipcode);
                }
                Log.i("HASHMAPBITCH", zipcodes.toString());

                mDatabase.child("zipcodes").removeEventListener(this);

//                postList.clear();
//                postListAdapter.notifyDataSetChanged();
//                Log.i("sort gets us here:", "1");
                Log.i("sort gets us here:", zipcodesToCompare.toString());
                for (String zipcodetocompare : zipcodesToCompare) {
                    Log.i("BITCH", zipcodetocompare);
                    Log.i("CUNT", zipCodesInRadius.toString());


                    if (zipCodesInRadius.contains(zipcodetocompare)) {
                        for (String postID : zipcodes.get(zipcodetocompare)) {
                            DatabaseReference postRef = mDatabase.child("posts").child(postID);
                            postRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.i("Bitch3x", dataSnapshot.toString());
                                    if (dataSnapshot != null) {
                                        Post post = new Post((HashMap<String, Object>) dataSnapshot.getValue());
                                        postList.add(post);
                                        Log.i("FUCK", post.toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        Collections.reverse(postList);
                        postListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
