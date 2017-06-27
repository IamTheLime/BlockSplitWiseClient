package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pojo.FriendDetailsGroupAdd;
import pojo.FriendInfo;
import pojo.GroupDetails;

public class GroupAdd extends AppCompatActivity {
    private String selected = null;
    private RecyclerView recyclerView;
    private List<FriendDetailsGroupAdd> friends;
    private List<FriendInfo> addedFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        CardView addFriend = (CardView) findViewById(R.id.addGroupCardview);
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
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardView button = (CardView) findViewById(R.id.addGroupCardview);
                Intent goToGroupDetails = new Intent(GroupAdd.this, AddFriendToGroup.class);
                startActivityForResult(goToGroupDetails, 0);
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
        addedFriends = new ArrayList<>();



    }


    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                FriendInfo myValue = (FriendInfo) data.getSerializableExtra("FriendName");
                // use 'myValue' return value here
                if(!addedFriends.contains(myValue)){
                    addedFriends.add(myValue);
                    friends.add(new FriendDetailsGroupAdd(myValue.getfriendName()));
                    recyclerView.setAdapter(new GroupCreationAdapter(LayoutInflater.from(this),friends,new GroupAdd.GroupRecyclerOnClickHandler(),getAssets()));
                }
            }
        }


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

    private class GroupRegisterer extends AsyncTask<String,Void,Boolean> {
        private String groupName;
        private String description;
        private ArrayList<String> members;
        private HttpURLConnection myConnection;
        /*
        * params[>1] - lista de utilizadores para adicionar ao grupo
        * params[0] - nome do grupo
        * params[1] - descricao do grupo
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            System.out.print("Im here");
            URL myEndpoint = null;
            groupName = params[0];
            description = params[1];
            members = new ArrayList<>();
            try {
                myEndpoint = new URL("http://" + R.string.connect + "/groupreg");}
            catch(Exception e) {
                e.printStackTrace();
                return false;
            }
            // Create connection
            myConnection = null;
            try{
                myConnection =
                        (HttpURLConnection) myEndpoint.openConnection();}
            catch (Exception e){
                e.printStackTrace();
                return false;}

            // Enable writing
            try{
                myConnection.setRequestMethod("POST");
                myConnection.setRequestProperty  ("Authorization", "Basic " + Base64.encodeToString(("null:null").getBytes(),Base64.DEFAULT));
                myConnection.setRequestProperty("Content-Type","application/json");
                String registerJSON = "{\"users\":[" + params[2];
                members.add(params[2]);
                for(int i = 3; i < params.length; i++) {
                    registerJSON = registerJSON + "," + params[i];
                    members.add(params[i]);
                }

                registerJSON = registerJSON + "],\"identifier\":\" "+ params[0] +" \",\"description\":\" " + params[1] + "\"}";
                byte[] outputInBytes = registerJSON.getBytes("UTF-8");
                OutputStream os = myConnection.getOutputStream();
                os.write(outputInBytes);
                os.flush();
                os.close();

                if (myConnection.getResponseCode() == 200) {

                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(params[0],params[1].toCharArray());
                        }
                    });
                    return true;

                }
                else if (myConnection.getResponseCode() == 409) {
                    return false;
                }
                else{
                    return false;
                }

            }
            catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            /*BufferedReader inHttp = null;
            try {
                inHttp = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String body = null;
            try {
                body = inHttp.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            if(aBoolean==true){

                GroupDetails gd = new GroupDetails(groupName,description,members,R.mipmap.ic_money);

                Intent intent = getIntent();
                intent.putExtra("group",gd);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else{
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
                return;
            }
        }
    }
    
}