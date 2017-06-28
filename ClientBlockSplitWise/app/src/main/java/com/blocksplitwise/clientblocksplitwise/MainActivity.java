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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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

import pojo.FriendInfo;
import pojo.GroupDetails;
import pojo.State;

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
        state = (State) bundle.get("state");
        if(state==null)
            state = new State(loginInfo, new ArrayList<FriendInfo>(), new ArrayList<GroupDetails>());
        MainActivity.GroupRegisterer gr = new MainActivity.GroupRegisterer();
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
            goToGroups.putExtra("state",state);
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


    private class GroupRegisterer extends AsyncTask<String,Void,Boolean> {
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
                myEndpoint = new URL("http://192.168.1.4:9000/users/"+user);}
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
            JSONArray jArr = null;

            try {
                jObj = new JSONObject(body);
                jArr = jObj.getJSONArray("groups");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < jArr.length(); i++) {
                try {
                    String group = (String) jArr.get(i);
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
                    Toast.makeText(MainActivity.this,"YOLO : " + s,Toast.LENGTH_SHORT).show();
                    MainActivity.GetGroups gg = new MainActivity.GetGroups();
                    try {
                        gg.execute("rui", s).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                /*for(String s: ids) {
                    groups.add(getGroupById(s));
                }
                if(groups.size()!=0)recyclerView.setAdapter(new GroupsPreviewAdapter(LayoutInflater.from(Groups.this),groups,new GroupRecyclerOnClickHandler(),getAssets()));*/

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

    private class GetGroups extends AsyncTask<String,Void,Boolean> {
        private String user;
        private String groupID;
        private HttpURLConnection myConnection;
        /*
        * params[0] - user
        * params[1] - groupid
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Im here");
            user = params[0];
            groupID = params[1];
            URL myEndpoint = null;
            try {
                myEndpoint = new URL("http://192.168.1.4:9000/groups/"+groupID);}
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
                JSONArray jArr = null;

                try {
                    jObj = new JSONObject(body);
                    jArr = jObj.getJSONArray("users");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String gname = null;
                String desc = null;
                ArrayList<String> members = null;
                try {
                    gname = jObj.getString("name");
                    desc = jObj.getString("desc");
                    members = new ArrayList<>();

                    for (int i = 0; i < jArr.length();i++) {
                        members.add((String) jArr.get(i));
                    }

                    GroupDetails gd;

                    gd = new GroupDetails(gname,desc,members,R.mipmap.ic_money);
                    state.addGroup(gd);
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
