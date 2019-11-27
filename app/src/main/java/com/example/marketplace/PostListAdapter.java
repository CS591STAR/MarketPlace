package com.example.marketplace;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView postName, askingPriceTxt, itemPriceTxt;
        public ImageView postImage;

        public MyViewHolder(View view) {
            super(view);
            postName = (TextView) view.findViewById(R.id.postName);
            askingPriceTxt = (TextView) view.findViewById(R.id.askingPriceTxt);
            itemPriceTxt = (TextView) view.findViewById(R.id.itemPriceTxt);
            postImage = (ImageView) view.findViewById(R.id.postImage);
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
        Post post = postList.get(position);
        holder.postName.setText(post.getItemName());
        holder.askingPriceTxt.setText(post.getAskingPrice());
//        holder.postImage.setImageURI();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
