package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Tiago-DESKTOP-WIN-PC on 17/05/13.
 */
public class GroupDetails implements Serializable {
    private String groupName;
    private String groupInfo;
    private String id;
    private ArrayList<String> members;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private int photoId;
    private String tstamp;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void addMembers(String... members) {
        for(int i = 0; i < members.length; i++)
            this.members.add(members[i]);
    }

    public ArrayList<String> getMembers() {
        ArrayList<String> res = new ArrayList<>();
        for(String s: members)
            res.add(s);
        return res;
    }

    public GroupDetails(String name, String groupInfo, String id, int photoId) {
        this.groupName = name;
        this.groupInfo = groupInfo;
        this.id = id;
        this.photoId = photoId;
        this.members = new ArrayList<>();
    }

    public GroupDetails(GroupDetails gd) {
        this.groupName = gd.getGroupName();
        this.groupInfo = gd.getGroupInfo();
        this.id = gd.getId();
        this.photoId = gd.getPhotoId();
        this.members = gd.getMembers();
    }

    public GroupDetails(String groupName, String groupInfo, ArrayList<String> members, String id, int photoId, String tstamp) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.id = id;
        this.photoId = photoId;
        this.tstamp = tstamp;
        this.members = new ArrayList<>();
        for (String s: members)
            this.members.add(s);
    }



    public GroupDetails(String groupName, String groupInfo, ArrayList<String> members, ArrayList<Transaction> transactions, String id, int photoId) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.id = id;
        this.photoId = photoId;
        this.members = new ArrayList<>();
        for (String s: members)
            this.members.add(s);

        this.transactions = new ArrayList<>();
        for (Transaction t: transactions)
            this.transactions.add(t);
    }

    public GroupDetails(String groupName, String groupInfo, ArrayList<String> members, int photoId) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.id = "0";
        this.photoId = photoId;
        this.members = new ArrayList<>();
        for (String s: members)
            this.members.add(s);
    }

    public GroupDetails clone() {
        return new GroupDetails(this);
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction ts, int x) {this.transactions.add(x,ts);}

    public void addTransaction(Transaction ts) {this.transactions.add(ts);}

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupDetails{");
        sb.append("groupName='").append(groupName).append('\'');
        sb.append(", groupInfo='").append(groupInfo).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", members=").append(members);
        sb.append(", transactions=").append(transactions);
        sb.append(", photoId=").append(photoId);
        sb.append('}');
        return sb.toString();
    }

    public String getTstamp() {
        return tstamp;
    }

    public void setTstamp(String tstamp) {
        this.tstamp = tstamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDetails that = (GroupDetails) o;
        return photoId == that.photoId &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(groupInfo, that.groupInfo) &&
                Objects.equals(id, that.id) &&
                Objects.equals(members, that.members) &&
                Objects.equals(transactions, that.transactions) &&
                Objects.equals(tstamp, that.tstamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, groupInfo, id, members, transactions, photoId, tstamp);
    }
}