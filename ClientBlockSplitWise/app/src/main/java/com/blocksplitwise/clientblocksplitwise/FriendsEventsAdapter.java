package com.blocksplitwise.clientblocksplitwise;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pojo.FriendInfo;

/**
 * Created by rui on 23-06-2017.
 */

public class FriendsEventsAdapter extends RecyclerView.Adapter<FriendsEventsAdapter.FriendViewHolder>{
    private List<FriendInfo> friends;
    private  View.OnClickListener clickListener = null;
    private final LayoutInflater inflater;
    private final AssetManager assets;

    public  class FriendViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView friendName;
        ImageView groupPhoto;


        FriendViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            friendName = (TextView)itemView.findViewById(R.id.friend_name);
            groupPhoto = (ImageView)itemView.findViewById(R.id.friend_foto);
            //Changing the fonts
            Typeface face = Typeface.createFromAsset(assets,
                    "font/pragmata.ttf");
            Typeface face2 = Typeface.createFromAsset(assets,
                    "font/pragmata.ttf");
            friendName.setTypeface(face2);
            friendName.setTextSize(24);

        }

    }

    FriendsEventsAdapter(LayoutInflater inflater, List<FriendInfo>friends, View.OnClickListener groch, AssetManager assets){
        this.friends = friends;
        this.clickListener = groch;
        this.inflater = inflater;
        this.assets = assets;
    }


    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public FriendsEventsAdapter.FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.card_view_friends, viewGroup, false);
        v.setOnClickListener(clickListener);
        FriendsEventsAdapter.FriendViewHolder gvh = new FriendsEventsAdapter.FriendViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(FriendsEventsAdapter.FriendViewHolder friendViewHolder, int i) {
        friendViewHolder.friendName.setText(friends.get(i).getfriendName());
        friendViewHolder.groupPhoto.setImageResource(friends.get(i).getPhotoId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}