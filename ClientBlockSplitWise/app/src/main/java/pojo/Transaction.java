package pojo;

/**
 * Created by rui on 29/06/2017.
 */

public class Transaction {

    private int value;
    private String user;
    private String fromUser;
    private String group;
    private String tstamp;
    private String message;
    private String checksum;

    public Transaction(int value, String user, String fromUser, String group, String tstamp, String message, String checksum) {
        this.value = value;
        this.user = user;
        this.fromUser = fromUser;
        this.group = group;
        this.tstamp = tstamp;
        this.message = message;
        this.checksum = checksum;
    }

    public int getValue() {
        return value;
    }

    public String getUser() {
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
}
