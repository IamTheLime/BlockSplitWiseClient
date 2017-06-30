package pojo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by rui on 29/06/2017.
 */

public class Transaction implements Serializable {

    private ArrayList<String> user;
    private ArrayList<Float> values;
    private String fromUser;
    private String group;
    private String tstamp;
    private String message;
    private String checksum;

    public Transaction(ArrayList<String> user, ArrayList<Float> values, String fromUser, String group, String message, String checksum, String timestamp) {
        this.user = user;
        this.values = values;
        this.fromUser = fromUser;
        this.group = group;
        this.tstamp = timestamp;
        this.message = message;
        this.checksum = checksum;
    }

    public Transaction(ArrayList<String> user, ArrayList<Float> values, String fromUser, String group,String message) {
        this.user = user;
        this.values = values;
        this.fromUser = fromUser;
        this.group = group;
        this.tstamp = "" + System.currentTimeMillis() + "s";
        this.message = message;
    }

    public ArrayList<String> getUser() {
        return user;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getGroup() {
        return group;
    }

    public String getTstamp() {
        return tstamp;
    }

    public String getMessage() {
        return message;
    }

    public String getChecksum() {
        return checksum;
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("user=").append(user);
        sb.append(", values=").append(values);
        sb.append(", fromUser='").append(fromUser).append('\'');
        sb.append(", group='").append(group).append('\'');
        sb.append(", tstamp='").append(tstamp).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", checksum='").append(checksum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
