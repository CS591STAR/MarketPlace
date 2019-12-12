package com.example.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ViewPost extends Fragment {

    Button deletePost;
    Button contactSeller;
    Button btnUser;
    TextView postTitle;
    TextView postPrice;
    TextView txtAmazon;
    TextView txtEbay;
    TextView postDescription;
    TextView conditionPost;
    TextView categoryPost;
    ImageView postImage;
    Post post;
    private Button btnReport;

    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseZip;
    private static final String TAG = "VIEW_POST";

    public ViewPost() {
        //Required empty public constructor
    }

    public ViewPost(Post post) {
        this.post = post;
    }

    public interface ViewPostListener {
        public void openFeed();

        public void openOtherProfile(String sellerID);
        // add methods that we would need the activity to implement
    }

    ViewPostListener VPL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        VPL = (ViewPostListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("posts");
        mDatabaseZip = FirebaseDatabase.getInstance().getReference().child("zipcodes");

        btnUser = view.findViewById(R.id.btnUser);
        deletePost = view.findViewById(R.id.DeletePost);
        contactSeller = view.findViewById(R.id.contactSellerPost);
        postTitle = view.findViewById(R.id.titlePost);
        postPrice = view.findViewById(R.id.pricePost);
        conditionPost = view.findViewById(R.id.conditionPost);
        postDescription = view.findViewById(R.id.descriptionPost);
        postImage = view.findViewById(R.id.imagePost);

        txtAmazon = view.findViewById(R.id.txtAmazon);
        txtEbay = view.findViewById(R.id.txtEbay);
        categoryPost = view.findViewById(R.id.categoryPost);
        btnReport = view.findViewById(R.id.btnReport);

        String EbayPrice = getString(R.string.no_result);
        String AmazonPrice = getString(R.string.no_result);


        if (!post.geteBayPrice().equals("")) {
            EbayPrice = post.geteBayPrice();
        }
        txtEbay.setText(txtEbay.getText().toString() + " " + EbayPrice);

        if (!post.getAmazonPrice().equals("")) {
            AmazonPrice = post.getAmazonPrice();
        }
        txtAmazon.setText(txtAmazon.getText().toString() + " " + AmazonPrice);

        // set post info
        postTitle.setText(post.getItemName());
        postDescription.setText(post.getItemDescription());

        Log.i("VIEW", "image link is: " + post.getImage());
        // display image
        GlideApp.with(this /* context */)
                .load(post.getImage())
                .into(postImage);

        conditionPost.setText(getResources().getStringArray(R.array.itemConditions)[Post.Condition.valueOf(post.getItemCondition()).ordinal() + 1]);

        categoryPost.setText(getResources().getStringArray(R.array.categories)[Post.Category.valueOf(post.getCategory()).ordinal() + 1]);

        String userPrice = postPrice.getText().toString() + " $" + post.getAskingPrice();
        postPrice.setText(userPrice);

        // update UI
        updateUI();

        contactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat();
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePostFromDB();
            }
        });

        //set listener for report btn
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), getResources().getString(R.string.thank_you_for_your_report), Toast.LENGTH_SHORT).show();
                btnReport.setEnabled(false);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VPL.openOtherProfile(post.getSellerID());
            }
        });

        return view;
    }

    private void startChat() {
        Intent intent = new Intent(getContext(), Chatroom.class);
        intent.putExtra(Chatroom.TALKER_ID, post.getSellerID());
        startActivity(intent);
    }

    private void deletePostFromDB() {
        // delete from posts db
        mDatabase.child(post.getPostID()).removeValue();
        // delete from zipcodes db
        mDatabaseZip.child(post.getZipcode()).child(post.getPostID()).removeValue();
        Toast.makeText(getContext(), getResources().getString(R.string.post_deleted_successfully), Toast.LENGTH_SHORT).show();
        VPL.openFeed();
    }

    private void updateUI() {
        if (mUsername.equals(post.getSellerID())) {
            deletePost.setVisibility(View.VISIBLE);
            contactSeller.setVisibility(View.GONE);
            btnUser.setVisibility(View.GONE);
            Log.i("POSTID", "sellerID= " + post.getSellerID() + " currentUserID " + mFirebaseUser.getUid());
        } else {
            deletePost.setVisibility(View.GONE);
            contactSeller.setVisibility(View.VISIBLE);
            btnUser.setVisibility(View.VISIBLE);
        }
    }
}
