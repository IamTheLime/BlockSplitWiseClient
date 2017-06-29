package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import pojo.GroupDetails;
import pojo.State;

public class Groups extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroupDetails gd;
    private List<String> groupsIDs;
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("GROUPS");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0, 50, 0, 0);
        final ImageButton buttonArrow;
        //Creating the action for the Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addGroupButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGroup = new Intent(Groups.this,GroupAdd.class);
                addGroup.putExtra("state",state);
                startActivityForResult(addGroup,0);
            }
        });
        //Creates popup button whenever the arrow is pressed, Still A WIP
        buttonArrow = (ImageButton) findViewById(R.id.imageButton);
        buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Groups.this, buttonArrow, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.show();
            }

        });
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
        /*try {
            getGroups();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        recyclerView.setAdapter(new GroupsPreviewAdapter(LayoutInflater.from(Groups.this),state.getGroups(),new GroupRecyclerOnClickHandler(),getAssets()));

        //EXTRAS
        initfonts();
    }

    private void initfonts(){
        Typeface face = Typeface.createFromAsset(getAssets(),
                "font/Amethyst.ttf");
        ((TextView)findViewById(R.id.textView2)).setTypeface(face);
        ((TextView)findViewById(R.id.textView3)).setTypeface(face);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                GroupDetails myValue = (GroupDetails) data.getSerializableExtra("group");
                // use 'myValue' return value here
                if(myValue!=null) {
                    state.addGroup(myValue);
                    recyclerView.setAdapter(new GroupsPreviewAdapter(LayoutInflater.from(Groups.this),state.getGroups(),new GroupRecyclerOnClickHandler(),getAssets()));
                }
            }
            else return;
        }


    }

    private void initializeData() {
        //Query the server for the group information
        //Initialize the List With The group details
        groupsIDs = new ArrayList<>();
        state = (State) getApplicationContext();
        /*groups.add(new GroupDetails("General Expenses1", "This is just a group", R.mipmap.ic_money));
        groups.add(new GroupDetails("General Expenses2", "This is just a group", R.mipmap.ic_money));
        groups.add(new GroupDetails("House Expenses1", "This is another group", R.mipmap.ic_house));
        groups.add(new GroupDetails("House Expenses2", "This is another group", R.mipmap.ic_house));
        groups.add(new GroupDetails("Party Group1", "This is yet another group", R.mipmap.ic_party));
        groups.add(new GroupDetails("Party Group1", "This is yet another group", R.mipmap.ic_party));*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("state",state);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }


    //Handler of the clicks

    private class GroupRecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            GroupDetails item = state.getGroupPosition(itemPosition);
            Intent goToGroupDetails = new Intent(Groups.this, Group.class);
            goToGroupDetails.putExtra("group", item);
            goToGroupDetails.putExtra("index", itemPosition);
            startActivityForResult(goToGroupDetails, 1);
        }
    }
}