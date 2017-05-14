package com.blocksplitwise.clientblocksplitwise;

import android.support.annotation.NonNull;
import com.alorma.timeline.TimelineView;

class Event {
    private String transaction;
    private int type;
    private int alignment;

    Event(@NonNull String transaction) {
        this(transaction, TimelineView.TYPE_DEFAULT);
    }

    Event(@NonNull String transaction, int type) {
        this(transaction, type, TimelineView.ALIGNMENT_DEFAULT);
    }

    Event(@NonNull String transaction, int type, int alignment) {
        this.transaction = transaction;
        this.type = type;
        this.alignment = alignment;
    }

    public String gettransaction() {
        return transaction;
    }

    public void settransaction(@NonNull String transaction) {
        this.transaction = transaction;
    }

    @TimelineView.TimelineType
    int getType() {
        return type;
    }

    public void setType(@TimelineView.TimelineType int type) {
        this.type = type;
    }

    @TimelineView.TimelineAlignment
    int getAlignment() {
        return alignment;
    }

    public void setAlignment(@TimelineView.TimelineAlignment int alignment) {
        this.alignment = alignment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("transaction='").append(transaction).append('\'');
        sb.append(", type=").append(type);
        sb.append(", alignment=").append(alignment);
        sb.append('}');
        return sb.toString();
    }
}