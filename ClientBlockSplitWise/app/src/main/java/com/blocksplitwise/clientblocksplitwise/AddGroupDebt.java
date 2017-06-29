package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
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

public class AddGroupDebt extends AppCompatActivity {
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
    private float[] shares;
    private TextWatcher textWatcher;

    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    public static boolean isFloat(String s)
    {
        return DOUBLE_PATTERN.matcher(s).matches();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_debt);

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

        initializeData();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AddGroupDebt.SpinnerActivity());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.debts_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //Code to Implement the scrollable groups
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

        /*Button button1 = (Button) findViewById(R.id.addFrienButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String text = tAmount.getText().toString();
                //End Parsing input
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);


                if(!text.equals("") && isFloat(text)) {
                    Float amount = Float.parseFloat(text);
                    FriendDebts fd =  new FriendDebts(option,amount,reportDate);
                    //Toast.makeText(AddGroupDebt.this, fd.toString(), Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, fd.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    /*Intent resultData = new Intent();
                    resultData.putExtra("debt", new FriendDebts(option,amount,reportDate));
                    setResult(Activity.RESULT_OK, resultData);
                    finish();*/
                /*}
                else {
                    Snackbar.make(view, "Please Insert A Valid Amount", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });*/
    }

    private void setHandlers() {
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(AddGroupDebt.this, editText.getText(), Toast.LENGTH_SHORT).show();
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
                if(editText.getSelectionStart() >= editText.getText().length())
                    editText.setSelection(editText.getText().length()-1);
            }
        });

        editText.addTextChangedListener(textWatcher);
    }

    private void setDivision(int option) {
        switch (option) {
            case 0:
                //split equally
                int totalM = groupId.getMembers().size();
                editText.setText(((1/totalM) * 100) + "%" );
                for(int i = 0; i < shares.length ; i++) {
                    shares[i] = (1/totalM) * 100;
                }
                editText.setText((((float) 1/totalM) * 100) + "%" );
                editText.setEnabled(false);
                payment = 0;
                break;
            case 1:
               //pay all the debt
                payment = 1;

                for(int i = 0; i < shares.length ; i++) {
                    shares[i] = 0.0f;
                }
                shares[ownerpos] = 100.f;
                editText.setEnabled(true);
                editText.setText("0%");
                editText.setEnabled(false);
                break;
            case 2:
                //set shares
                payment = 2;
                editText.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void initializeData() {
        Bundle bundle = getIntent().getExtras();
        groupId = (GroupDetails) bundle.get("group");
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
            Toast.makeText(AddGroupDebt.this, "Item "+pos,Toast.LENGTH_SHORT).show();
            setDivision(pos);
            /*if(st.equals("You owe")) {
                option = true;
            }
            if(st.equals("Owes you"))
                option = false;
            Toast.makeText(AddGroupDebt.this, "Item "+option,Toast.LENGTH_SHORT).show();*/
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
            GroupDetails item = state.getGroupPosition(itemPosition);
            DecimalFormat df;
            switch (payment) {
                case 0:
                    break;
                case 1:
                    editText.setEnabled(true);
                    df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    editText.setText(df.format(shares[itemPosition]) + "%");
                    editText.setEnabled(false);
                    break;
                case 2:
                    editText.setEnabled(true);
                    df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    editText.setText(df.format(shares[itemPosition]) + "%");
                    break;
            }
            editText.clearFocus();
        }
    }
}
