package com.blocksplitwise.clientblocksplitwise;

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
import com.alorma.timeline.RoundTimelineView;
import com.alorma.timeline.TimelineView;

import java.util.ArrayList;

import pojo.GroupDetails;

public class Group extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GroupDetails gd = (GroupDetails) getIntent().getSerializableExtra("GroupValue");
        //Creating the timeline

        RoundTimelineView timelineView = (RoundTimelineView) findViewById(R.id.timeline);
        //Glide.with(this).load(R.drawable.avatar).into(timelineView);
        RecyclerView list = (RecyclerView) findViewById(R.id.rv);
        list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
       // RecyclerView.ItemDecoration itemDecoration = new
         //       DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //list.addItemDecoration(itemDecoration);
        list.setLayoutManager(llm);
        ArrayList<Event> items = new ArrayList<>();
        items.add(new Event("FirstTest", TimelineView.TYPE_START));
        items.add(new Event("Teste4", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste3", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste4", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste3", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste4", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste3", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste4", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste3", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("Teste4", RoundTimelineView.TYPE_MIDDLE));
        items.add(new Event("LastTest", RoundTimelineView.TYPE_END));
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new EventsAdapter(LayoutInflater.from(this), items));

    }



}
