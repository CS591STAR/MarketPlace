package com.example.marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatList extends AppCompatActivity {

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatTextView;
        CircleImageView chatImageView;

        public ChatViewHolder(View v) {
            super(v);
            chatTextView = (TextView) itemView.findViewById(R.id.chatTextView);
            chatImageView = (CircleImageView) itemView.findViewById(R.id.chatImageView);
        }
    }

    private static final String TAG = "ChatList";
    public static final String MESSAGES_CHILD = "messages";

    private RecyclerView chatlistRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<SingleChat, ChatList.ChatViewHolder>
            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        //enable return button in title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Initialize RecyclerView.
        chatlistRecyclerView = (RecyclerView) findViewById(R.id.chatlistRecyclerView);
        chatlistRecyclerView.addItemDecoration(new DividerItemDecoration(chatlistRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        chatlistRecyclerView.setLayoutManager(mLinearLayoutManager);
        mProgressBar = (ProgressBar) findViewById(R.id.pbChatList);

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<SingleChat> parser = new SnapshotParser<SingleChat>() {
            @Override
            public SingleChat parseSnapshot(DataSnapshot dataSnapshot) {
                SingleChat singleChat = dataSnapshot.getValue(SingleChat.class);
                if (singleChat != null) {
                    singleChat.setId(dataSnapshot.getKey());
                }
                return singleChat;
            }
        };
        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(mFirebaseUser.getUid());
        FirebaseRecyclerOptions<SingleChat> options =
                new FirebaseRecyclerOptions.Builder<SingleChat>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<SingleChat, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder viewHolder, int position, @NonNull final SingleChat singleChat) {

                //disable progress bar
                mProgressBar.setVisibility(View.INVISIBLE);
                viewHolder.chatTextView.setText(singleChat.getName());
                if (singleChat.getPhotoUrl() == null) {
                    viewHolder.chatImageView.setImageDrawable(ContextCompat.getDrawable(ChatList.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(ChatList.this)
                            .load(singleChat.getPhotoUrl())
                            .into(viewHolder.chatImageView);
                }
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), Chatroom.class);
                        intent.putExtra("talkToID", singleChat.getId());
                        intent.putExtra("talkToName", singleChat.getName());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_chat, parent, false);
                return new ChatViewHolder(v);
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int chatCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (chatCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    chatlistRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        chatlistRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
