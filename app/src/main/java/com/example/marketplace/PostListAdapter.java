package com.example.marketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> postList = new ArrayList<>();

    public PostListAdapter(@NonNull Context context, @LayoutRes ArrayList<Post> list) {
        super(context, 0, list);
        mContext = context;
        postList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem = convertView;
        if (listitem == null){
            listitem = LayoutInflater.from(mContext).inflate(R.layout.marketfeed_listrow,parent,false);
        }

        Post currentPost = postList.get(position);
        TextView postName = listitem.findViewById(R.id.postName);
        TextView askingPriceTxt = listitem.findViewById(R.id.askingPriceTxt);
        TextView itemPriceTxt = listitem.findViewById(R.id.itemPriceTxt);
        ImageView postImage = listitem.findViewById(R.id.postImage);

        return listitem;
    }

}
