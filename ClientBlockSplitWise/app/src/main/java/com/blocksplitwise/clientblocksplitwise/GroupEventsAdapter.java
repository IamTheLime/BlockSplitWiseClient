package com.blocksplitwise.clientblocksplitwise;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alorma.timeline.RoundTimelineView;
import com.alorma.timeline.TimelineView;

import java.util.List;

import pojo.Transaction;


class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolderItem> {

    private final LayoutInflater inflater;
    private final List<Transaction> events;
    private  View.OnClickListener clickListener = null;
    private final AssetManager assets;
    EventsAdapter(LayoutInflater inflater, List<Transaction> events, View.OnClickListener clickListener, AssetManager assets) {
        this.inflater = inflater;
        this.events = events;
        this.clickListener = clickListener;
        this.assets = assets;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.card_view_timeline, parent, false);
        v.setOnClickListener(clickListener);
        ViewHolderItem vhi = new ViewHolderItem(v);
        return vhi;

    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        Transaction event = events.get(position);
        holder.test1.setText(event.getFromUser());
        holder.test2.setText("" + event.getValues().get(0));
        holder.test3.setImageResource(R.drawable.ic_menu_groups);
        if(position == 0)
            holder.timeline.setTimelineType(TimelineView.TYPE_START);
        else if (position == events.size())
            holder.timeline.setTimelineType(RoundTimelineView.TYPE_END);
        else holder.timeline.setTimelineType(RoundTimelineView.TYPE_MIDDLE);
        holder.timeline.setTimelineAlignment(TimelineView.ALIGNMENT_DEFAULT);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

     class ViewHolderItem extends RecyclerView.ViewHolder {
        CardView cv;
        TextView test1;
        TextView test2;
        ImageView test3;

        TimelineView timeline;

        ViewHolderItem(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvtimeline);
            test1 = (TextView) itemView.findViewById(R.id.group_info2);
            test2 = (TextView) itemView.findViewById(R.id.group_name2);
            test3 = (ImageView) itemView.findViewById(R.id.group_photo2);
            timeline = (TimelineView) itemView.findViewById(R.id.timeline);

            //Changing the fonts
            Typeface face = Typeface.createFromAsset(assets,
                    "font/pragmata.ttf");
            Typeface face2 = Typeface.createFromAsset(assets,
                    "font/pragmata.ttf");
            test1.setTypeface(face);
            test1.setTextSize(18);
            test2.setTypeface(face2);
            test2.setTextSize(20);

        }

     }
}