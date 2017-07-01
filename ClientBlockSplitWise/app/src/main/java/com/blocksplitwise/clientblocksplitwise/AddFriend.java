package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import pojo.FriendInfo;
import pojo.State;

public class AddFriend extends AppCompatActivity {
    private TextView email;
    private State state;
    private FriendInfo fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("Add Some Friends");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        state = (State) getApplicationContext();
        email = (TextView) findViewById(R.id.email);

        Button button1 = (Button) findViewById(R.id.addFrienButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = email.getText().toString();

                if(!text.equals("")) {
                    /*Snackbar.make(view, email.getText(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    fi = new FriendInfo(text, R.drawable.ic_menu_friends);
                    AddFriend.AddFriendRegisterer afr = new AddFriend.AddFriendRegisterer();
                    try {
                        afr.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent resultData = new Intent();
                    resultData.putExtra("friend", text);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                }
                else {
                    Snackbar.make(view, "Please Insert A Valid User Name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        return true;
    }

    private class AddFriendRegisterer extends AsyncTask<String,Void,Boolean> {
        private HttpURLConnection myConnection;
        /*
        * params[4] - lista de utilizadores, concatenada numa string, para adicionar ao grupo
        * params[0] - username
        * params[1] - password
        * params[2] - nome do grupo
        * params[3] - descricao do grupo
        * */
        @Override
        protected Boolean doInBackground(final String... params) {
            URL myEndpoint = null;
            try {
                myEndpoint = new URL("http://"+getString(R.string.connection)+":9000/addfriend");}
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
                myConnection.setRequestProperty("Content-Type","application/json");
                String registerJSON = "{\"user_id\":\"" + state.getUserName() + "\",\"friend_id\":\""+ fi.getfriendName() + "\"}";
                System.out.println(registerJSON);
                byte[] outputInBytes = registerJSON.getBytes("UTF-8");
                OutputStream os = myConnection.getOutputStream();
                os.write(outputInBytes);
                os.flush();
                os.close();

                if (myConnection.getResponseCode() == 200) {
                    return true;

                }
                else if (myConnection.getResponseCode() == 409) {
                    return false;
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
                state.addFriend(fi);
                Intent intent = getIntent();
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
