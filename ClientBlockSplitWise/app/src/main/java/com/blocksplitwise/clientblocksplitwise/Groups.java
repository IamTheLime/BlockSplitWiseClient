package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

import pojo.GroupDetails;
import pojo.State;
import pojo.Transaction;

public class Groups extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroupDetails gd;
    private List<String> groupsIDs;
    private State state;
    private TextView tv3;



    @Override
    protected void onPostResume() {
        super.onPostResume();
        Groups.UpdateGRoups ug = new Groups.UpdateGRoups();
        try {
            ug.execute(state.getUserName(),state.getUserTs()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

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
        tv3 = (TextView) findViewById(R.id.textView3);
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

    private class UpdateGRoups extends AsyncTask<String,Void,Boolean> {
        private String user;
        private String password;
        private String tstamp;
        BufferedReader inHttp = null;
        /*
        * params[0] - user
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            System.out.print("Im here");
            user = params[0];
            tstamp = params[1];
            URL myEndpoint = null;
            HttpURLConnection myConnection = null;
            try {
                myEndpoint = new URL("http://"+getResources().getString(R.string.connection)+":9000/users/"+user+"/"+(Integer.parseInt(tstamp) + 1));}
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
            // Enable writing
            try{
                myConnection.setRequestMethod("GET");

                if (myConnection.getResponseCode() == 200) {
                    try {
                        inHttp = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;

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

        private ArrayList<String> getGroupsByIdentifier() {
            ArrayList<String> res = new ArrayList<>();

            String body = null;
            try {
                body = inHttp.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jObj =null;
            JSONArray jArrMembers = null;
            String bstamp = null;

            try {
                jObj = new JSONObject(body);
                jArrMembers = jObj.getJSONArray("groups");
                bstamp = jObj.getString("bstamp");
                if(jArrMembers.length() <= 0)
                    return null;
                if(Integer.parseInt(bstamp) < Integer.parseInt(tstamp))
                    return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < jArrMembers.length(); i++) {
                try {
                    String group = (String) jArrMembers.get(i);
                    res.add(group);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            state.setUserTs(bstamp);
            return res;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if(aBoolean==true){
                groupsIDs = getGroupsByIdentifier();

                if(groupsIDs==null)
                    return;


                for(String s: groupsIDs) {
                    Groups.GetGroup gg = new Groups.GetGroup();
                    try {
                        gg.execute(state.getUserName(), s).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                return;
            }
            else{
                /*Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
                return;*/
            }
        }
    }

    private class GetGroup extends AsyncTask<String,Void,Boolean> {
        private String user;
        private String groupID;
        private HttpURLConnection myConnection;
        private Transaction transaction;
        /*
        * params[0] - user
        * params[1] - groupid
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            user = params[0];
            groupID = params[1];
            URL myEndpoint = null;
            try {
                myEndpoint = new URL("http://"+getResources().getString(R.string.connection)+":9000/groups/"+groupID);}
            catch(Exception e) {
                e.printStackTrace();
            }
            // Create connection
            myConnection = null;
            try{
                myConnection =
                        (HttpURLConnection) myEndpoint.openConnection();
            }
            catch (Exception e){
                e.printStackTrace();}

            // Enable writing
            // Enable writing
            try{
                myConnection.setRequestMethod("GET");

                if (myConnection.getResponseCode() == 200) {
                    return true;

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

            if(aBoolean==true){
                BufferedReader inHttp = null;
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
                }

                JSONObject jObj =null;
                JSONArray jArrMembers = null;
                JSONArray jArrTrans = null;

                try {
                    jObj = new JSONObject(body);
                    jArrMembers = jObj.getJSONArray("users");
                    jArrTrans = jObj.getJSONArray("transactions");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String gname = null;
                String desc = null;
                String id = null;
                String tstamp = null;
                ArrayList<String> members = null;
                try {
                    gname = jObj.getString("name");
                    desc = jObj.getString("desc");
                    id = jObj.getString("ident");
                    tstamp = jObj.getString("bstamp");

                    members = new ArrayList<>();

                    for (int i = 0; i < jArrMembers.length();i++) {
                        members.add((String) jArrMembers.get(i));
                    }

                    GroupDetails gd;
                    gd = new GroupDetails(gname,desc,members,id,R.mipmap.ic_money,tstamp);
                    state.addGroup(gd);

                    int pos = state.groupIndex(gd);

                    ArrayList<Transaction> ts = new ArrayList<>();


                    for (int i = 0; i < jArrTrans.length();i++) {
                        Groups.GetTransactions gt = new Groups.GetTransactions();
                        gt.execute((String) jArrTrans.get(i), ""+pos).get();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                return;
            }
            else{
                /*Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
                return;*/
            }
        }
    }


    private class GetTransactions extends AsyncTask<String,Void,Boolean> {
        private String transId;
        private int groupId;
        private HttpURLConnection myConnection;
        /*
        * params[0] - user
        * params[1] - groupid
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            transId = params[0];
            groupId = Integer.parseInt(params[1]);
            URL myEndpoint = null;
            try {
                myEndpoint = new URL("http://"+getResources().getString(R.string.connection)+":9000/transactions/"+transId);}
            catch(Exception e) {
                e.printStackTrace();
            }
            // Create connection
            myConnection = null;
            try{
                myConnection =
                        (HttpURLConnection) myEndpoint.openConnection();
            }
            catch (Exception e){
                e.printStackTrace();}

            // Enable writing
            // Enable writing
            try{
                myConnection.setRequestMethod("GET");

                if (myConnection.getResponseCode() == 200) {
                    return true;

                }
                else{
                    System.out.println(myConnection.getResponseMessage());
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

            if(aBoolean==true){
                BufferedReader inHttp = null;
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
                }

                JSONObject jObj =null;
                JSONArray jArrVAlues = null;

                try {
                    jObj = new JSONObject(body);
                    jArrVAlues = jObj.getJSONArray("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String gname = null;
                String desc = null;
                String id = null;
                String fromUser = null;
                String timestamp = null;
                ArrayList<String> users = new ArrayList<>();
                ArrayList<Float> values = new ArrayList<>();
                try {
                    gname = jObj.getString("group");
                    desc = jObj.getString("message");
                    id = jObj.getString("chksum");
                    fromUser = jObj.getString("fromUser");
                    timestamp = jObj.getString("tstamp");

                    for(int i = 0; i < jArrVAlues.length(); i++){
                        JSONObject temp = jArrVAlues.getJSONObject(i);
                        users.add(temp.getString("person"));
                        values.add((float) temp.getDouble("amount"));
                    }

                    Transaction res = new Transaction(users,values,fromUser,gname,desc,id,timestamp);
                    state.addTransaction(groupId,res);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return;
            }
            else{
                /*Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
                return;*/
            }
        }
    }

    private float changeSettledText(){
        float sum = 0.f;
        List<GroupDetails> groups = state.getGroups();
        for(GroupDetails g : groups){
            for(Transaction t : g.getTransactions()){
                ArrayList<String> users = t.getUser();
                int index = users.indexOf(state.getUserName());
                sum += t.getValues().get(index);
                System.out.println("OLA " + sum);

            }
        }

        return sum;
    }
}