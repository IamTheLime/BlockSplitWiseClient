package com.blocksplitwise.clientblocksplitwise;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pojo.GroupDetails;
import pojo.State;
import pojo.Transaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private State state;
    private ArrayList<String> groupsIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        state = (State) getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("BLOCKSPLITWISE");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Bundle bundle = getIntent().getExtras();
        String loginInfo = bundle.getString("Login");
        //state = new State(loginInfo, new ArrayList<FriendInfo>(), new ArrayList<GroupDetails>());
        state.setUserName(loginInfo);
        MainActivity.GetGroups gr = new MainActivity.GetGroups();
        try {
            gr.execute(loginInfo).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.navbarTitle)).setText("BlockSplitwise Client");
        ((TextView) findViewById(R.id.textView)).setText(loginInfo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.friends) {
            Intent goToFriends = new Intent(this,Friends.class);
            startActivityForResult(goToFriends,0);
        } else if (id == R.id.groups) {
            Intent goToGroups = new Intent(this,Groups.class);
            startActivityForResult(goToGroups,0);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        // Make sure the request was successful
        if (resultCode == RESULT_OK) {
            //There are no intent with data
        }
        if (resultCode == RESULT_CANCELED){
            //Just do nothing
        }
    }

    public interface AsyncResponse {
        void processFinish(Transaction output, int pos);
    }

    private class GetGroups extends AsyncTask<String,Void,Boolean> {
        private String user;
        private String password;
        BufferedReader inHttp = null;
        /*
        * params[0] - user
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            System.out.print("Im here");
            user = params[0];
            URL myEndpoint = null;
            HttpURLConnection myConnection = null;
            try {
                myEndpoint = new URL("http://"+getResources().getString(R.string.connection)+":9000/users/"+user);}
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

            try {
                jObj = new JSONObject(body);
                jArrMembers = jObj.getJSONArray("groups");
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
            return res;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if(aBoolean==true){
                groupsIDs = getGroupsByIdentifier();


                for(String s: groupsIDs) {
                    MainActivity.GetGroup gg = new MainActivity.GetGroup();
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

    private class GetGroup extends AsyncTask<String,Void,Boolean>{
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
                ArrayList<String> members = null;
                try {
                    gname = jObj.getString("name");
                    desc = jObj.getString("desc");
                    id = jObj.getString("ident");

                    members = new ArrayList<>();

                    for (int i = 0; i < jArrMembers.length();i++) {
                        members.add((String) jArrMembers.get(i));
                    }

                    GroupDetails gd;
                    gd = new GroupDetails(gname,desc,members,id,R.mipmap.ic_money);
                    state.addGroup(gd);

                    int pos = state.groupIndex(gd);

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

                    Transaction res = new Transaction(users,values,fromUser,gname,desc,id);
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


}
