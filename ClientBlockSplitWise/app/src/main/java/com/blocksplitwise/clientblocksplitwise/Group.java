package com.blocksplitwise.clientblocksplitwise;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alorma.timeline.RoundTimelineView;
import com.alorma.timeline.TimelineView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import pojo.FriendDebts;
import pojo.GroupDetails;

public class Group extends AppCompatActivity {
    private RecyclerView list;
    private ArrayList<Event> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("GROUP");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDebt = new Intent(Group.this,AddGroupDebt.class);
                startActivityForResult(addDebt,0);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GroupDetails gd = (GroupDetails) getIntent().getSerializableExtra("GroupValue");
        //Creating the timeline

        RoundTimelineView timelineView = (RoundTimelineView) findViewById(R.id.timeline);
        //Glide.with(this).load(R.drawable.avatar).into(timelineView);
        list = (RecyclerView) findViewById(R.id.rv);
        list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
       // RecyclerView.ItemDecoration itemDecoration = new
         //       DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //list.addItemDecoration(itemDecoration);
        list.setLayoutManager(llm);
        fillItems();
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new EventsAdapter(LayoutInflater.from(this), items, new EventClickHandler(),getAssets()));
        CircularImageView iv = (CircularImageView) findViewById(R.id.group_logo);
        iv.setImageResource(R.mipmap.ic_launcher);
        iv.setElevation(60);
                //iv.setImageResource(R.mipmap.ic_launcher);

        //Extras
        initfonts();
    }

    /* ---------> Handler de criar uma nova divida para o grupo <--------
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
                    // Adicionar as dividas
                    items.add(myValue);
                    recyclerView.setAdapter(new FriendEventAdapter(LayoutInflater.from(this),friends,new Friend.RecyclerOnClickHandler(),getAssets()));
                }
            }
        }


    }*/


    private void initfonts(){
        Typeface face = Typeface.createFromAsset(getAssets(),
                "font/pragmata.ttf");
        ((Button)findViewById(R.id.balance)).setTypeface(face);
        ((Button)findViewById(R.id.balance)).setTextSize(16);
        ((Button)findViewById(R.id.settled)).setTypeface(face);
        ((Button)findViewById(R.id.settled)).setTextSize(16);
    }

    private void fillItems(){
        items = new ArrayList<>();
        items.add(new Event("01-02-2017", TimelineView.TYPE_START));
        items.add(new Event("02-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("03-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("04-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("05-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("06-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("07-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("08-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("09-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("10-02-2017", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("11-02-2017", RoundTimelineView.TYPE_END));
    }

    protected class EventClickHandler implements RecyclerView.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = list.getChildLayoutPosition(view);
            Event event = items.get(itemPosition);
            Toast.makeText(Group.this, event.toString(), Toast.LENGTH_SHORT).show();
           // Intent goToGroupDetails = new Intent(Group.this,Group.class);
            //goToGroupDetails.putExtra("GroupValue",item);
           // startActivityForResult(goToGroupDetails,0);
        }
    }

}
