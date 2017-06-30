package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import pojo.FriendDebts;
import pojo.FriendInfo;
import pojo.GroupDetails;
import pojo.State;
import pojo.Transaction;

public class AddGroupDebt extends AppCompatActivity {
    private int index;
    private TextView tAmount;
    private Spinner tDebt;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private State state;
    private EditText editText;
    private GroupDetails groupId;
    private EditText amount;
    private int payment = 0;
    private int ownerpos = 0;
    private int selected = 0;
    private float total;
    private float[] shares;
    private TextWatcher textWatcher;
    private TextInputLayout textInputLayout;
    private Transaction ts;
    DecimalFormat df;

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
        initializeData();
        setContentView(R.layout.activity_add_group_debt);
        df = new DecimalFormat("#.##");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("Add some Debt");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AddGroupDebt.SpinnerActivity());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.debts_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //Code to Implement the scrollable groups
        textInputLayout = (TextInputLayout) findViewById(R.id.picker);
        editText = (EditText) findViewById(R.id.percentage);
        amount = (EditText) findViewById(R.id.amount);
        editText.setImeActionLabel("Select", KeyEvent.KEYCODE_ENTER);
        payment = 0;
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        // This is Just for Test
        recyclerView.setAdapter(new FriendEventAdapter(LayoutInflater.from(this),groupId.getMembers(),new AddGroupDebt.GroupRecyclerOnClickHandler(),getAssets()));

        setHandlers();
    }

    private float getTotal() {
        float res= 0.f;
        for(int i = 0; i < shares.length; i++)
            res += shares[i];
        return res;
    }

    private void setHandlers() {
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                float per;
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //Toast.makeText(AddGroupDebt.this, editText.getText(), Toast.LENGTH_SHORT).show();
                    String input = editText.getText().toString();
                    if (isFloat(input.replaceAll("%",""))) {
                        per = Float.parseFloat(input.replaceAll("%",""));
                        shares[selected] = per;
                        total = getTotal();
                        textInputLayout.setHint(df.format((1 - total/100) * 100) + "% left");
                        editText.clearFocus();
                        InputMethodManager imm =  (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                        Toast.makeText(AddGroupDebt.this, "" + total, Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(AddGroupDebt.this, "Input must be a decimal number", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });



        Button button1 = (Button) findViewById(R.id.addDebtButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().equals("") || !isFloat(amount.getText().toString())) {
                    final Snackbar snackbar  = Snackbar.make(recyclerView, "Amount:Please insert a decimal number", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                }
                            }).show();
                    return;
                }
                String input = editText.getText().toString();
                if(total < 100) {
                    if (isFloat(input.replaceAll("%",""))) {
                        float per;
                        per = Float.parseFloat(input.replaceAll("%",""));
                        shares[selected] = per;
                        total = getTotal();
                        textInputLayout.setHint(df.format((1 - total/100) * 100) + "% left");
                        editText.clearFocus();
                        InputMethodManager imm =  (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                        Toast.makeText(AddGroupDebt.this, "Total " + total, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Snackbar.make(recyclerView, "Sum of all percentages must add to 100", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                }
                float fAmount;
                if (isFloat(amount.getText().toString().replaceAll("%","")))
                    fAmount = Float.parseFloat(amount.getText().toString());
                else{
                    Snackbar.make(recyclerView, "Amount:Please insert a decimal number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                ArrayList<String> user = new ArrayList<>();
                for(String usn: groupId.getMembers())
                    user.add(usn);
                ArrayList<Float> values = new ArrayList<>();
                for(int  i = 0; i < shares.length; i++) {
                    values.add(fAmount * (float) Math.ceil(shares[i])/100);
                }
                String creator = state.getUserName();
                String group = groupId.getId();
                ts = new Transaction(user,values,creator,group,"");
                AddGroupDebt.TransRegisterer agd = new AddGroupDebt.TransRegisterer();
                agd.execute();
            }
        });

        amount.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                float per;
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                    // Perform action on key press
                    //Toast.makeText(AddGroupDebt.this, editText.getText(), Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.removeTextChangedListener(textWatcher);
                String text = s.toString();
                text = text.replaceAll("%","");
                text = text + "%";
                editText.setText(text);
                editText.setSelection(text.length()-1);
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                editText.addTextChangedListener(textWatcher);
                return;
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Toast.makeText(getApplicationContext(), "KJSADHGJKASDHUIGASHIU", Toast.LENGTH_SHORT).show();
                if(editText.getSelectionEnd() >= editText.getText().length() - 1)
                    editText.setSelection(editText.getText().length()-1);
            }
        });

        editText.addTextChangedListener(textWatcher);
    }

    private void setDivision(int option) {
        DecimalFormat df;
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        switch (option) {
            case 0:
                //split equally
                int totalM = groupId.getMembers().size();
                float per =  (1.f/(float)totalM) * 100.f;
                for(int i = 0; i < shares.length ; i++) {
                    shares[i] = per;
                }
                editText.setEnabled(true);
                textInputLayout.setHint("Percentage");
                editText.setText(per + "%");
                editText.setEnabled(false);
                total = getTotal();
                payment = 0;
                break;
            case 1:
               //pay all the debt
                payment = 1;

                for(int i = 0; i < shares.length ; i++) {
                    shares[i] = 0.0f;
                }
                shares[ownerpos] = 100.f;
                total = getTotal();
                editText.setEnabled(true);
                textInputLayout.setHint("Percentage");
                editText.setText("0%");
                editText.setEnabled(false);
                break;
            case 2:
                //set shares
                payment = 2;
                editText.setEnabled(true);
                Snackbar.make(recyclerView, "Please Insert A Valid Amount", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                for(int i = 0; i < shares.length ; i++) {
                    shares[i] = 0.f;
                }
                total = getTotal();
                textInputLayout.setHint(df.format((1 - total/100) * 100) + "% left");
                editText.setText("%");
                break;
            default:
                break;
        }
    }

    private void initializeData() {
        Bundle bundle = getIntent().getExtras();
        groupId = (GroupDetails) bundle.get("group");
        index = bundle.getInt("index");
        state = (State) getApplicationContext();
        ownerpos = groupId.getMembers().indexOf(state.getUserName());
        shares = new float[groupId.getMembers().size()];

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        return true;
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            //String st = (String) parent.getItemAtPosition(pos);
            Toast.makeText(AddGroupDebt.this, "Item " + pos,Toast.LENGTH_SHORT).show();
            setDivision(pos);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
            /*option = true;
            Toast.makeText(AddGroupDebt.this, "Nothing "+option,Toast.LENGTH_SHORT).show();*/
        }
    }

    private class GroupRecyclerOnClickHandler implements RecyclerView.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            switch (payment) {
                case 0:
                    break;
                case 1:
                    editText.setEnabled(true);
                    editText.setText(df.format(shares[itemPosition]) + "%");
                    editText.setEnabled(false);
                    break;
                case 2:
                    editText.setEnabled(true);
                    if(!editText.getText().toString().equals("") && !editText.getText().toString().matches("0*%?")) {
                        if(selected!= itemPosition) {
                            float per;
                            String input = editText.getText().toString();
                            if (isFloat(input.replaceAll("%",""))) {
                                per = Float.parseFloat(input.replaceAll("%",""));
                                shares[selected] = per;
                                total = getTotal();
                                textInputLayout.setHint(df.format((1 - total/100) * 100) + "% left");
                                editText.clearFocus();
                                InputMethodManager imm =  (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                                Toast.makeText(AddGroupDebt.this, "" + total, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    editText.setText(df.format(shares[itemPosition]) + "%");
                    break;
            }
            selected = itemPosition;
            editText.clearFocus();
        }
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
                                        "\"vals\":[ { \"person\": \"" + ts.getUser().get(0) + "\",\"amount\":" + ts.getValues().get(0) + "}" ;
                for(int i = 1; i < ts.getUser().size(); i++) {
                    registerJSON = registerJSON + ",{ \"person\": \"" + ts.getUser().get(i) + "\",\"amount\":" + ts.getValues().get(i) + "}" ;
                }
                registerJSON += "],\"msg\":\"" + ts.getMessage() + "\",\"ts\":\"" + ts.getTstamp() +"\"}";
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
