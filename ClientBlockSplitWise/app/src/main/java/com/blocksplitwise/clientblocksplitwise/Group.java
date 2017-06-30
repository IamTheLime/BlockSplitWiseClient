package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import pojo.FriendDebts;
import pojo.GroupDetails;
import pojo.State;
import pojo.Transaction;

public class Group extends AppCompatActivity {
    private int index;
    private RecyclerView list;
    private ArrayList<Event> items;
    private State state;
    private int length;
    private GroupDetails groupName;



    @Override
    protected void onResume() {
        super.onResume();
        GetGroup ug = new GetGroup();
        try {
            ug.execute(state.getUserName(),groupName.getId(),groupName.getTstamp()).get();
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
                addDebt.putExtra("group",groupName);
                addDebt.putExtra("index",index);
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
        orderTimestap();
        length = state.getGroupPosition(index).getTransactions().size();
        list.setAdapter(new EventsAdapter(LayoutInflater.from(this), state.getGroupPosition(index).getTransactions(), new EventClickHandler(),getAssets()));
        CircularImageView iv = (CircularImageView) findViewById(R.id.group_logo);
        iv.setImageResource(R.mipmap.ic_launcher);
        iv.setElevation(60);
                //iv.setImageResource(R.mipmap.ic_launcher);

        //Extras
        initfonts();
    }

    // ---------> Handler de criar uma nova divida para o grupo <--------
    /*protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                FriendDebts myValue = (FriendDebts) data.getSerializableExtra("debt");
                // use 'myValue' return value here
                if(myValue!=null) {
                    state = (State) getApplicationContext();
                    orderTimestap();
                    list.setAdapter(new EventsAdapter(LayoutInflater.from(this), state.getGroupPosition(index).getTransactions(), new EventClickHandler(),getAssets()));
                }
            }
        }


    }*/

    private void orderTimestap() {
        Collections.sort(state.getGroupPosition(index).getTransactions(), new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                if(Long.parseLong(o1.getTstamp().replaceAll("s","")) >= Long.parseLong(o2.getTstamp().replaceAll("s","")))
                    return 1;
                else return -1;
            }
        });
    }


    private void initfonts(){
        Typeface face = Typeface.createFromAsset(getAssets(),
                "font/pragmata.ttf");
        ((Button)findViewById(R.id.settled)).setTypeface(face);
        ((Button)findViewById(R.id.settled)).setTextSize(16);
    }

    private void fillItems(){
        items = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        groupName = (GroupDetails) bundle.get("group");
        index = bundle.getInt("index");
        state = (State) getApplicationContext();

    }

    protected class EventClickHandler implements RecyclerView.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = list.getChildLayoutPosition(view);
            Transaction event = state.getGroupPosition(index).getTransactions().get(itemPosition);
            Toast.makeText(Group.this, event.toString(), Toast.LENGTH_SHORT).show();
           // Intent goToGroupDetails = new Intent(Group.this,Group.class);
            //goToGroupDetails.putExtra("GroupValue",item);
           // startActivityForResult(goToGroupDetails,0);
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }



    private class GetGroup extends AsyncTask<String,Void,Boolean> {
        private String user;
        private String groupID;
        private String tstamp;
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
            tstamp = params[2];
            URL myEndpoint = null;
            try {
                myEndpoint = new URL("http://"+getResources().getString(R.string.connection)+":9000/groups/"+groupID+"/"+(Integer.parseInt(tstamp) + 1) );}
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
                System.out.println("Consegui ir buscar os grpos");
                BufferedReader inHttp = null;
                try {
                    inHttp = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String body = null;
                try {
                    body = inHttp.readLine();
                    System.out.println(body);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject jObj =null;
                JSONArray jArrTrans = null;

                try {
                    jObj = new JSONObject(body);
                    jArrTrans = jObj.getJSONArray("transactions");
                    if(jArrTrans.length()<=0)
                        return;
                    System.out.println("Recebi novas transacoes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String bstamp = null;
                try {
                    bstamp = jObj.getString("bstamp");
                    System.out.println(bstamp + " dasngkjfdhgujiadhgjlkdaflkja ##" + tstamp);
                    if(Integer.parseInt(bstamp) <= Integer.parseInt(tstamp))
                        return;
                    groupName.setTstamp(bstamp);

                    int pos = index;

                    ArrayList<Transaction> ts = new ArrayList<>();


                    for (int i = 0; i < jArrTrans.length();i++) {
                        GetTransactions gt = new GetTransactions();
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

                    state.addTransaction(groupId,length,res);
                    list.setAdapter(new EventsAdapter(LayoutInflater.from(Group.this), state.getGroupPosition(index).getTransactions(), new EventClickHandler(),getAssets()));


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

}
