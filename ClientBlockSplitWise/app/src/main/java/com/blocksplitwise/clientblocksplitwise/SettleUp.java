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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import pojo.GroupDetails;
import pojo.State;
import pojo.Transaction;

public class SettleUp extends AppCompatActivity {
    private TextView debt;
    private GroupDetails group;
    private int index;
    private EditText amount;
    private Button settle;
    private Transaction ts;
    private State state;

    public boolean isFloat(String s)
    {
        float res;
        try {
            res = Float.parseFloat(s);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("Settle");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        debt = (TextView) findViewById(R.id.money);
        amount = (EditText) findViewById(R.id.amount);
        settle = (Button) findViewById(R.id.butom);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        group = (GroupDetails) bundle.get("group");
        index = bundle.getInt("index");
        state = (State) getApplicationContext();
        settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> users = new ArrayList<>();
                ArrayList<Float> values = new ArrayList<>();
                users.add(state.getUserName());
                String text = amount.getText().toString();
                float val;
                if(isFloat(text))
                    val = Float.parseFloat(text);
                else
                {
                    final Snackbar snackbar  = Snackbar.make(getCurrentFocus(), "Amount:Please insert a decimal number", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
                    return;
                }

                if(val > Float.parseFloat(debt.getText().toString().replaceAll("€",""))){

                    final Snackbar snackbar  = Snackbar.make(getCurrentFocus(), "You're paying to much", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
                    return;
                }
                val = - val;
                values.add(val);
                ts = new Transaction(users,values,state.getUserName(),group.getId(),"");
                SettleUp.TransRegisterer tr = new SettleUp.TransRegisterer();
                try {
                    tr.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private class TransRegisterer extends AsyncTask<String,Void,Boolean> {
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
                myEndpoint = new URL("http://"+getString(R.string.connection)+":9000/newtransaction");}
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
                //String registerJSON = "{\"users\":[" + gMembers + "],\"identifier\":\""+ groupName +"\",\"gname\":\""+ groupName +"\",\"description\":\"" + description + "\"}";
                String registerJSON = "{\"usr\":\"" + ts.getFromUser() + "\"," +
                        "\"grp\":\"" + ts.getGroup()+"\"," +
                        "\"vals\":[ { \"person\": \"" + ts.getUser().get(0) + "\",\"amount\":" + ts.getValues().get(0) + "}" +
                        "],\"msg\":\"" + ts.getMessage() + "\",\"ts\":\"" + ts.getTstamp() +"\"}";
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
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if(aBoolean==true){
                Intent intent = getIntent();
                intent.putExtra("group",group);
                intent.putExtra("index",index);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("group",group);
                returnIntent.putExtra("index",index);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
                return;
            }
        }
    }

}
