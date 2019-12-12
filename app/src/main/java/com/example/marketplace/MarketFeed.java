package com.example.marketplace;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MarketFeed extends Fragment {

    Button btnCreatePost;
    SeekBar distanceSeekBar;
    Button filterByDistanceBtn;
    String mileRadius;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String userZipcode;

    private Button btnSortByPrice;
    private Spinner sprCategory;
    private ProgressBar pbFeed;
    private boolean sortByPriceAscending = true;

    private DatabaseReference mDatabase;

    ArrayList<String> zipCodesInRadius = new ArrayList<>();
    ArrayList<String> zipcodesToCompare = new ArrayList<>();

    Map<String, ArrayList<String>> zipcodes = new HashMap<>();

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private ValueEventListener basicValueEventListener;
    private ValueEventListener reverseValueEventListener;
    private Query currentQuery;

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
        mDatabase = FirebaseDatabase.getInstance().getReference(); // get the ref of db
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = mFirebaseUser.getUid();

        // get the user's zipcode
        mDatabase.child("users").child(mUsername).child("zip").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userZipcode = dataSnapshot.getValue().toString();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        buildZipcodeMap();

        btnCreatePost = view.findViewById(R.id.btnCreatePost);
        // init the adapter
        postListAdapter = new PostListAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postListAdapter); // set the adapter to the recycler view
        filterByDistanceBtn = view.findViewById(R.id.filterByDistanceBtn);
        distanceSeekBar = view.findViewById(R.id.distanceSeekBar);
        pbFeed = view.findViewById(R.id.pbFeed);

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
        //hide seek bar before use it
        distanceSeekBar.setVisibility(View.GONE);
        filterByDistanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceSeekBar.setVisibility(View.VISIBLE);
            }
        });

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

                    mileRadius = String.valueOf(seekBar.getProgress());
                    Toast.makeText(getContext(), "Search radius set is " + mileRadius + " from your own zipcode", Toast.LENGTH_LONG).show();

                    if (mileRadius.equals("0")) {
                        postList.clear();
                        sortByPostTime();
                        postListAdapter.notifyDataSetChanged();
                    } else {
                        zipcodesInRadius();
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
                    currentQuery.removeEventListener(reverseValueEventListener);
                }
                if (i == 0) {
                    sortByPostTime();
                } else {
                    currentQuery = mDatabase.child("posts").orderByChild("category").equalTo(Post.Category.values()[i - 1].toString());
                    currentQuery.addValueEventListener(basicValueEventListener);
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

        //initialize a default event listener
        if (basicValueEventListener == null) {

            basicValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    postList.clear();
                    pbFeed.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.hasChildren()) {
                        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                        while (iter.hasNext()) {
                            DataSnapshot snap = iter.next();
                            String postID = snap.getKey();
                            try {

                            Post post = new Post((HashMap<String, Object>) snap.getValue());
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

            };
        }

        if(reverseValueEventListener == null){
            reverseValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    postList.clear();
                    pbFeed.setVisibility(View.INVISIBLE);

                    if (dataSnapshot.hasChildren()) {
                        Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                        while (iter.hasNext()) {
                            DataSnapshot snap = iter.next();
                            String postID = snap.getKey();
                            try {

                                Post post = new Post((HashMap<String, Object>) snap.getValue());
                                postList.add(post);
                                //received results
                                Log.i("post", post.getItemName() + " on nod " + postID);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // notify the adapter
                    postListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
        }

        return view;
    }

    private void sortByPrice() {
        sortByPriceAscending = !sortByPriceAscending;
        currentQuery.removeEventListener(basicValueEventListener);
        currentQuery.removeEventListener(reverseValueEventListener);
        currentQuery = mDatabase.child("posts").orderByChild("askingPrice");
        if(sortByPriceAscending){
            currentQuery.addValueEventListener(reverseValueEventListener);
        }
        else {
            currentQuery.addValueEventListener(basicValueEventListener);
        }
    }

    private void sortByPostTime() {
        if(currentQuery != null) {
            currentQuery.removeEventListener(basicValueEventListener);
            currentQuery.removeEventListener(reverseValueEventListener);
        }
        currentQuery = mDatabase.child("posts").orderByChild("itemPostTime");
        currentQuery.addValueEventListener(basicValueEventListener);
    }

    public Query getCurrentQuery() {
        return currentQuery;
    }

    private void buildZipcodeMap() {

        // clear both lists
        zipcodes.clear();
        zipcodesToCompare.clear();

        mDatabase.child("zipcodes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentZipCode = snapshot.getKey(); // Key for hashmap
                    Log.i("CURR", "current zipcode is " + currentZipCode);
                    zipcodesToCompare.add(currentZipCode);
                    ArrayList<String> postsInZipcode = new ArrayList<>();
                    for (DataSnapshot zipcodeChildren : snapshot.getChildren()) {
                        postsInZipcode.add(zipcodeChildren.getKey());
                    }
                    zipcodes.put(currentZipCode, postsInZipcode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void zipcodesInRadius() {
        Log.i("SORT", "sort by distance");

        // clear lists
        zipCodesInRadius.clear();

        String redLineAPIEndPoint = "https://redline-redline-zipcode.p.rapidapi.com/rest/radius.json/" + userZipcode + "/" + mileRadius + "/mile";

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
        recyclerView.getRecycledViewPool().clear();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postListAdapter.notifyDataSetChanged();
            }
        });
            for (String zipcodetocompare : zipcodesToCompare) {
            Log.i("SINGLE", "single zipcode is " + zipcodetocompare);
            Log.i("Radius", "zipcodes returned from API " + zipCodesInRadius.toString());
            if (zipCodesInRadius.contains(zipcodetocompare)) {
                for (String postID : zipcodes.get(zipcodetocompare)) {
                    Log.i("FETCH", "getting post with id " + postID + " from db");
                    DatabaseReference postRef = mDatabase.child("posts").child(postID);
                    postRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                try {
                                    Post post = new Post((HashMap<String, Object>) dataSnapshot.getValue());
                                    postList.add(post);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            postListAdapter.notifyDataSetChanged();
                                        }
                                    });
                                } catch (NullPointerException e) {
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    postRef.removeEventListener(basicValueEventListener);
                }
            }
        }
    }
}
