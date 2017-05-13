package com.blocksplitwise.clientblocksplitwise;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pojo.GroupDetails;

public class Groups extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<GroupDetails> groups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setPadding(0,50,0,0);
        final ImageButton buttonArrow;
        //Creates popup button whenever the arrow is pressed, Still A WIP
        buttonArrow = (ImageButton) findViewById(R.id.imageButton);
        buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Groups.this,buttonArrow, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                popup.show();
            }

        });
        //Code to Implement the scrollable groups
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        // This is Just for Test
        initializeData();
        RVAdapter adapter = new RVAdapter(groups);
        recyclerView.setAdapter(adapter);


    }

    private void initializeData(){
        groups = new ArrayList<>();
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Emma Wilson", "23 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lavery Maiss", "25 years old", R.drawable.ic_arrow_drop_down_black_24dp));
        groups.add(new GroupDetails("Lillie Watts", "35 years old", R.drawable.ic_arrow_drop_down_black_24dp));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        return true;
    }

    ////////////////////////////// PRIVATE CLASSES//////////////////////////////////////////////////

    //These class assures that the cards are properly represented

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.GroupViewHolder>{
        private List<GroupDetails> groups;
        private final View.OnClickListener clickListener = new GroupRecyclerOnClickHandler();


        public  class GroupViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView groupName;
            TextView groupInfo;
            ImageView groupPhoto;

            GroupViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                groupName = (TextView)itemView.findViewById(R.id.group_name);
                groupInfo = (TextView)itemView.findViewById(R.id.group_info);
                groupPhoto = (ImageView)itemView.findViewById(R.id.group_photo);
            }

        }

        RVAdapter(List<GroupDetails> groups){
            this.groups = groups;
        }


        @Override
        public int getItemCount() {
            return groups.size();
        }

        @Override
        public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
            v.setOnClickListener(clickListener);
            GroupViewHolder gvh = new GroupViewHolder(v);
            return gvh;
        }

        @Override
        public void onBindViewHolder(GroupViewHolder personViewHolder, int i) {
            personViewHolder.groupName.setText(groups.get(i).getGroupName());
            personViewHolder.groupInfo.setText(groups.get(i).getGroupInfo());
            personViewHolder.groupPhoto.setImageResource(groups.get(i).getPhotoId());
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }
    //Handler of the clicks

    private class GroupRecyclerOnClickHandler implements RecyclerView.OnClickListener{
        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            GroupDetails item = groups.get(itemPosition);
            Intent goToGroupDetails = new Intent(Groups.this,Group.class);
            goToGroupDetails.putExtra("GroupValue",item);
            startActivityForResult(goToGroupDetails,0);
        }
    }

    //////////////////////////////////// END OF THE ADAPTER CARD CLASS//////////////////////////////


}
