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

import org.w3c.dom.Text;

import java.util.List;

import pojo.FriendDebts;
import pojo.FriendInfo;
import pojo.GroupDetails;

/**
 * Created by rui on 24/06/2017.
 */

public class FriendEventAdapter extends RecyclerView.Adapter<FriendEventAdapter.FriendViewHolder>{
    private List<String> friends;
    private  View.OnClickListener clickListener = null;
    private final LayoutInflater inflater;
    private final AssetManager assets;

    protected class FriendViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView amount;
        TextView date;


        FriendViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            amount = (TextView)itemView.findViewById(R.id.amount);
            //Changing the fonts
            Typeface face = Typeface.createFromAsset(assets,
                    "font/pragmata.ttf");
            amount.setTypeface(face);
            amount.setTextSize(24);

        }

    }

    FriendEventAdapter(LayoutInflater inflater, List<String>friends, View.OnClickListener groch, AssetManager assets){
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
    public FriendEventAdapter.FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.card_view_friend, viewGroup, false);
        v.setOnClickListener(clickListener);
        FriendEventAdapter.FriendViewHolder gvh = new FriendEventAdapter.FriendViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(FriendEventAdapter.FriendViewHolder friendViewHolder, int i) {
        friendViewHolder.amount.setText(friends.get(i));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}