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

import pojo.GroupDetails;

/**
 * Created by Tiago-DESKTOP-WIN-PC on 17/05/21.
 */


class GroupsPreviewAdapter extends RecyclerView.Adapter<GroupsPreviewAdapter.GroupViewHolder>{
        private List<GroupDetails> groups;
        private  View.OnClickListener clickListener = null;
        private final LayoutInflater inflater;
        private final AssetManager assets;

        public  class GroupViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView groupName;
            TextView groupInfo;
            ImageView groupPhoto;


            GroupViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                groupName = (TextView)itemView.findViewById(R.id.group_name);
                groupInfo = (TextView)itemView.findViewById(R.id.group_info);
                groupPhoto = (ImageView)itemView.findViewById(R.id.group_photo);
                //Changing the fonts
                Typeface face = Typeface.createFromAsset(assets,
                        "font/pragmata.ttf");
                Typeface face2 = Typeface.createFromAsset(assets,
                        "font/pragmata.ttf");
                groupName.setTypeface(face2);
                groupName.setTextSize(24);
                groupInfo.setTypeface(face);
                groupInfo.setTextSize(18);

            }

        }

        GroupsPreviewAdapter(LayoutInflater inflater, List<GroupDetails>groups, View.OnClickListener groch,AssetManager assets){
            this.groups = groups;
            this.clickListener = groch;
            this.inflater = inflater;
            this.assets = assets;
        }


        @Override
        public int getItemCount() {
            return groups.size();
        }

        @Override
        public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.card_view_layout, viewGroup, false);
            v.setOnClickListener(clickListener);
            GroupViewHolder gvh = new GroupViewHolder(v);
            return gvh;
        }

        @Override
        public void onBindViewHolder(GroupViewHolder groupViewHolder, int i) {
            groupViewHolder.groupName.setText(groups.get(i).getGroupName());
            groupViewHolder.groupInfo.setText(groups.get(i).getGroupInfo());
            groupViewHolder.groupPhoto.setImageResource(groups.get(i).getPhotoId());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

}

