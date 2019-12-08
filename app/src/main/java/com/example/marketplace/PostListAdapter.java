package com.example.marketplace;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {

    private List<Post> postList = new ArrayList<>();
    public Post post;
    ViewPost viewPost;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView postName, askingPriceTxt, itemPriceTxt;
        public ImageView postImage;


        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            postName = (TextView) view.findViewById(R.id.postName);
            askingPriceTxt = (TextView) view.findViewById(R.id.askingPriceTxt);
            itemPriceTxt = (TextView) view.findViewById(R.id.itemPriceTxt);
            postImage = (ImageView) view.findViewById(R.id.postImage);
        }

        @Override
        public void onClick(View view) {
            post = postList.get(getLayoutPosition());
            Log.d("Clicked", "onClick " + getLayoutPosition() + " " + post.getItemName());

            viewPost = new ViewPost(post);
            MainActivity activity = (MainActivity) view.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragLayout, viewPost)
                    .addToBackStack(null).commit();

        }
    }

    public PostListAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marketfeed_listrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        post = postList.get(position);
        holder.postName.setText(post.getItemName());
        holder.itemPriceTxt.setText(Long.toString(post.getAskingPrice()));
        GlideApp.with(context)
                .load(post.getImage())
                .into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
