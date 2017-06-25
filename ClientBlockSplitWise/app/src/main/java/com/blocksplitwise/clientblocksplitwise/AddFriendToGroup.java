package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pojo.FriendInfo;

public class AddFriendToGroup extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FriendInfo> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("Add a Friend");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        //Code to Implement the scrollable groups
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        // This is Just for Test
        initializeData();
        recyclerView.setAdapter(new FriendsEventsAdapter(LayoutInflater.from(this),friends,new AddFriendToGroup.RecyclerOnClickHandler(),getAssets()));

        //EXTRAS
        //initfonts();

    }

    private void initfonts(){
        Typeface face = Typeface.createFromAsset(getAssets(),
                "font/pragmata.ttf");
        ((TextView)findViewById(R.id.textView2)).setTypeface(face);
        ((TextView)findViewById(R.id.textView3)).setTypeface(face);
    }

    private void initializeData() {
        //Query the server for the group information
        //Initialize the List With The group details
        friends = new ArrayList<>();
        friends.add(new FriendInfo("Rui", R.mipmap.ic_money));
        friends.add(new FriendInfo("Tiago", R.mipmap.ic_money));
        friends.add(new FriendInfo("Bernardo", R.mipmap.ic_house));
        friends.add(new FriendInfo("Rafa", R.mipmap.ic_house));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",result);
        //Pode ser activity.result_OK
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        return true;
    }

    private class RecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            FriendInfo item = friends.get(itemPosition);
            Intent resultData = new Intent();
            resultData.putExtra("FriendName", new FriendInfo(item.getfriendName(),item.getPhotoId()));
            setResult(Activity.RESULT_OK, resultData);
            finish();
        }
    }

}
