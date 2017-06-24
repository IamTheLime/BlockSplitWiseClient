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

import pojo.FriendDetailsGroupAdd;


/**
 * Created by tiago on 6/24/17.
 */

public class GroupCreationAdapter extends RecyclerView.Adapter<GroupCreationAdapter.GroupAddFriendsViewHolder>{
        private List<FriendDetailsGroupAdd> friends;
        private  View.OnClickListener clickListener = null;
        private final LayoutInflater inflater;
        private final AssetManager assets;

        public  class GroupAddFriendsViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView friendName;


            GroupAddFriendsViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv3);
                friendName = (TextView)itemView.findViewById(R.id.friend_name);
                //Changing the fonts
                Typeface face = Typeface.createFromAsset(assets,
                        "font/pragmata.ttf");
                friendName.setTypeface(face);
                friendName.setTextSize(18);

            }

        }

        GroupCreationAdapter(LayoutInflater inflater, List<FriendDetailsGroupAdd> friends, View.OnClickListener groch, AssetManager assets){
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
        public GroupAddFriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.card_view_layout_add_group, viewGroup, false);
            v.setOnClickListener(clickListener);
            GroupAddFriendsViewHolder gvh = new GroupAddFriendsViewHolder(v);
            return gvh;
        }

        @Override
        public void onBindViewHolder(GroupAddFriendsViewHolder groupAddFriendsViewHolder, int i) {
            groupAddFriendsViewHolder.friendName.setText(friends.get(i).getFriendName());

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }



