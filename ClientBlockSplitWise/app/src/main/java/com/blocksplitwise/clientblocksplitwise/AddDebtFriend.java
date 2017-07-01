package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pojo.FriendDebts;

public class AddDebtFriend extends AppCompatActivity {
    private TextView tAmount;
    private Spinner tDebt;
    private boolean option = true;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt_friend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /////////////////////////////FONT TOP///////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.toolbar_title);
        Typeface face = Typeface.createFromAsset((getAssets()),"font/Amethyst.ttf");
        tv.setTypeface(face);
        tv.setText("Add a Friend");
        tv.setTextSize(30);
        ////////////////////////////////////////////////////////////////////////////
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);



        tAmount = (TextView) findViewById(R.id.amount);
        tDebt = (Spinner) findViewById(R.id.debt);
        tDebt.setOnItemSelectedListener(new AddDebtFriend.SpinnerActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.debts_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tDebt.setAdapter(adapter);


        Button button1 = (Button) findViewById(R.id.addFrienButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tAmount.getText().toString();
                //End Parsing input
                Float amount = Float.parseFloat(text);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);
                

                if(!text.equals("")) {
                    Intent resultData = new Intent();
                    resultData.putExtra("debt", new FriendDebts(option,amount,reportDate));
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                }
                else {
                    Snackbar.make(view, "Please Insert A Valid Amount", Snackbar.LENGTH_LONG)
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

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            String st = (String) parent.getItemAtPosition(pos);
            if(st.equals("You owe"))
                option = true;
            if(st.equals("Owes you"))
                option = false;
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
            option = true;
        }
    }
}
