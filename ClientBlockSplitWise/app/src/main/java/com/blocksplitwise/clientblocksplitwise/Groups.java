package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Groups extends AppCompatActivity {
    private CardView cardView;
    private CardView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);
        final ImageButton buttonArrow;
        //Creates popup button whenever the arrow is pressed, Still A WIP
        buttonArrow = (ImageButton) findViewById(R.id.imageButton);
        buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Groups.this,buttonArrow, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                popup.show();
            }

        });
        //Code to Implement the scrollable groups
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        return true;
    }



}
