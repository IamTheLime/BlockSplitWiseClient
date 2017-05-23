package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pojo.GroupDetails;

public class Groups extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<GroupDetails> groups;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        recyclerView.setAdapter(new GroupsPreviewAdapter(LayoutInflater.from(this),groups,new GroupRecyclerOnClickHandler(),getAssets()));

        //EXTRAS
        initfonts();
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
        groups = new ArrayList<>();
        groups.add(new GroupDetails("General Expenses1", "This is just a group", R.mipmap.ic_money));
        groups.add(new GroupDetails("General Expenses2", "This is just a group", R.mipmap.ic_money));
        groups.add(new GroupDetails("House Expenses1", "This is another group", R.mipmap.ic_house));
        groups.add(new GroupDetails("House Expenses2", "This is another group", R.mipmap.ic_house));
        groups.add(new GroupDetails("Party Group1", "This is yet another group", R.mipmap.ic_party));
        groups.add(new GroupDetails("Party Group1", "This is yet another group", R.mipmap.ic_party));

    }

    private class loadData extends AsyncTask<String,Void,Boolean> {
        private String email;
        private String password;
        @Override
        protected Boolean doInBackground(final String... params) {
            URL myEndpoint = null;
            email = params[0];password=params[1];
            try {
                myEndpoint = new URL("http://10.0.2.2:9000/users/rui");}catch(Exception e) {e.printStackTrace();}
            // Create connection
            HttpURLConnection myConnection = null;
            try{
                myConnection =
                        (HttpURLConnection) myEndpoint.openConnection();}catch (Exception e){e.printStackTrace();}

            // Enable writing
            try{
                myConnection.setRequestMethod("GET");
                myConnection.setRequestProperty  ("Authorization", "Basic " + Base64.encodeToString((params[0]+":"+params[1]).getBytes(),Base64.DEFAULT));

                if (myConnection.getResponseCode() == 200) {
                    //showProgress(true);
                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(params[0],params[1].toCharArray());
                        }
                    });
                    return true;

                }
                else{
                    return false;
                }

            }catch(Exception e){e.printStackTrace();return false;}
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }


    //Handler of the clicks

    private class GroupRecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            GroupDetails item = groups.get(itemPosition);
            Intent goToGroupDetails = new Intent(Groups.this, Group.class);
            goToGroupDetails.putExtra("GroupValue", item);
            startActivityForResult(goToGroupDetails, 0);
        }
    }
}