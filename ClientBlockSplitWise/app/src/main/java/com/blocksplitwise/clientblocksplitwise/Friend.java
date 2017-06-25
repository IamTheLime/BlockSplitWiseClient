package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pojo.FriendDebts;
import pojo.FriendInfo;

public class Friend extends AppCompatActivity {

    private TextView mTextMessage;
    private List<FriendDebts> debts;
    private RecyclerView recyclerView;
    private TextView friendName;
    private TextView balance;
    private TextView euro;
    private float debt = 0.0f;

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
        tv.setText("Add a Debt");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFriend = new Intent(Friend.this,AddDebtFriend.class);
                startActivityForResult(addFriend,0);
            }
        });

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

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        euro = (TextView) findViewById(R.id.euro);
        euro.setTypeface(face);
        euro.setText(""+df.format(debt)+"€");
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
        //recyclerView.setAdapter(new FriendEventAdapter(LayoutInflater.from(this),friends,new Friend.RecyclerOnClickHandler(),getAssets()));
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                FriendDebts myValue = (FriendDebts) data.getSerializableExtra("debt");
                // use 'myValue' return value here
                if(myValue!=null) {
                    //Ver se foi para pagar ou receber
                    if(myValue.isDebt())
                        debt -= myValue.getAmount();
                    else
                        debt += myValue.getAmount();
                    debts.add(myValue);
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    euro = (TextView) findViewById(R.id.euro);
                    euro.setText(""+df.format(debt)+"€");
                    recyclerView.setAdapter(new FriendEventAdapter(LayoutInflater.from(this),debts,new Friend.RecyclerOnClickHandler(),getAssets()));
                }
            }
        }


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
        debts = new ArrayList<>();

    }

    private class RecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            FriendDebts item = debts.get(itemPosition);
            //Toast.makeText(Friend.this, item.toString(),Toast.LENGTH_SHORT).show();
            Snackbar.make(view, item.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}
