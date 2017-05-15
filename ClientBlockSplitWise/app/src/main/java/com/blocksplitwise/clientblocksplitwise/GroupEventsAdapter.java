package com.blocksplitwise.clientblocksplitwise;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alorma.timeline.TimelineView;

import java.util.List;


class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolderItem> {

    private final LayoutInflater inflater;
    private final List<Event> events;

    EventsAdapter(LayoutInflater inflater, List<Event> events) {
        this.inflater = inflater;
        this.events = events;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderItem(inflater.inflate(R.layout.card_view_timeline, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        Event event = events.get(position);

        holder.test1.setText(event.gettransaction());
        holder.test2.setText(event.gettransaction());
        holder.test3.setImageResource(R.drawable.ic_menu_groups);
        holder.timeline.setTimelineType(event.getType());
        holder.timeline.setTimelineAlignment(event.getAlignment());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolderItem extends RecyclerView.ViewHolder {
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

        }
    }
}