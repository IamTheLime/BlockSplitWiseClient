package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pojo.FriendDebts;
import pojo.FriendInfo;

public class Friend extends AppCompatActivity {

    private TextView mTextMessage;
    private List<FriendDebts> friends;
    private RecyclerView recyclerView;
    private TextView friendName;
    private TextView balance;
    private TextView euro;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    recyclerView.setVisibility(View.GONE);
                    balance.setVisibility(View.VISIBLE);
                    euro.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    balance.setVisibility(View.GONE);
                    euro.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("FRIEND");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        /*mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setTypeface(face);
        mTextMessage.setText("Por aqui cenas relacionadas com a divida?");*/

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        friendName = (TextView) findViewById(R.id.userFriend);
        friendName.setTypeface(face);
        friendName.setText("Friend username");
        friendName.setTextSize(20);

        balance = (TextView) findViewById(R.id.balance);
        balance.setTypeface(face);
        balance.setText("Your balance with this friend is:");
        balance.setTextSize(30);

        Random random=new Random();
        float randomFloat = random.nextFloat() * 1000 - 500;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        euro = (TextView) findViewById(R.id.euro);
        euro.setTypeface(face);
        euro.setText(""+df.format(randomFloat)+"€");
        euro.setTextSize(100);



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
        recyclerView.setAdapter(new FriendEventAdapter(LayoutInflater.from(this),friends,new Friend.RecyclerOnClickHandler(),getAssets()));
        recyclerView.setVisibility(View.GONE);
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

    private void initializeData() {
        //Query the server for the group information
        //Initialize the List With The group details
        friends = new ArrayList<>();
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));
        friends.add(new FriendDebts(-10,"01-11-2041"));
        friends.add(new FriendDebts(5,"01-11-2021"));
        friends.add(new FriendDebts(-5,"01-11-2067"));
        friends.add(new FriendDebts(5, "01-11-2754"));

    }

    private class RecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            FriendDebts item = friends.get(itemPosition);
            //Toast.makeText(Friend.this, item.toString(),Toast.LENGTH_SHORT).show();
            Snackbar.make(view, item.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
