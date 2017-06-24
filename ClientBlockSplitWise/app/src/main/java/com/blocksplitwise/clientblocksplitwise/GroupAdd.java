package com.blocksplitwise.clientblocksplitwise;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pojo.FriendDetailsGroupAdd;
import pojo.GroupDetails;

public class GroupAdd extends AppCompatActivity {
    private String selected = null;
    private RecyclerView recyclerView;
    private List<FriendDetailsGroupAdd> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()), "font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("GROUP ADD");
        tv.setTextSize(30);

        TextView friendName = (TextView) findViewById(R.id.friend_name);

        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0, 50, 0, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        makeClickableButtons();
        //ADD PEOPLE TO GROUP
        //CREATE A RECICLERVIEWER TO SHOW THOSE PEOPLE
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
        recyclerView.setAdapter(new GroupCreationAdapter(LayoutInflater.from(this), friends, new GroupRecyclerOnClickHandler(), getAssets()));
        /////////////////////////////////////////////////

    }

    private void makeClickableButtons() {
        Button button1 = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) findViewById(R.id.button);
                resetColors();
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.accent));
                selected = button.getText().toString();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) findViewById(R.id.button2);
                resetColors();
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.accent));
                selected = button.getText().toString();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) findViewById(R.id.button3);
                resetColors();
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.accent));
                selected = button.getText().toString();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) findViewById(R.id.button4);
                resetColors();
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.accent));
                selected = button.getText().toString();
            }
        });
    }

    private void resetColors() {
        Button button = null;
        if (selected != null) {
            if (selected.equals("Apartment")) button = (Button) findViewById(R.id.button);
            if (selected.equals("House")) button = (Button) findViewById(R.id.button2);
            if (selected.equals("Trip")) button = (Button) findViewById(R.id.button3);
            if (selected.equals("Other")) button = (Button) findViewById(R.id.button4);
            if (button != null)
                button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryDark));
        }
    }


///////////////////////////////THIS IS JUST FOR NOW


    private void initializeData() {
        //Query the server for the group information
        //Initialize the List With The group details
        friends = new ArrayList<>();
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Rui"));
        friends.add(new FriendDetailsGroupAdd("Rafa"));
        friends.add(new FriendDetailsGroupAdd("Bernas"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Rui"));
        friends.add(new FriendDetailsGroupAdd("Rafa"));
        friends.add(new FriendDetailsGroupAdd("Bernas"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));
        friends.add(new FriendDetailsGroupAdd("Tiago"));



    }
    private class GroupRecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            FriendDetailsGroupAdd item = friends.get(itemPosition);
            Toast.makeText(GroupAdd.this, "TEHE", Toast.LENGTH_SHORT).show();
            //Intent goToGroupDetails = new Intent(Groups.this, Group.class);
            //goToGroupDetails.putExtra("GroupValue", item);
            //startActivityForResult(goToGroupDetails, 0);
        }
    }
}