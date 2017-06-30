package pojo;

import android.app.Application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 28/06/2017.
 */

public class State extends Application implements Serializable{

    private String userName;
    private List<FriendInfo> friends;
    private String userTs;
    private List<GroupDetails> groups;

    public State() {
        userName = null;
        friends = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public State(String userName, List<FriendInfo> friends, List<GroupDetails> groups, String userTs) {
        this.userName = userName;
        this.friends = friends;
        this.groups = groups;
        this.userTs = userTs;
    }

    public List<FriendInfo> getFriends() {
        ArrayList<FriendInfo> res = new ArrayList<>();
        for(FriendInfo fi: friends)
            res.add(fi.clone());
        return res;
    }

    public List<GroupDetails> getGroups() {
        ArrayList<GroupDetails> res = new ArrayList<>();
        for(GroupDetails gd: groups)
            res.add(gd.clone());
        return res;
    }

    public int groupIndex(GroupDetails gd) {
        return groups.indexOf(gd);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public synchronized void addGroup(GroupDetails gd) {
        groups.add(gd);
    }

    public synchronized void addFriend(FriendInfo fi) {
        friends.add(fi);
    }

    public synchronized void addTransaction(int gd, int x, Transaction ts) {
        GroupDetails aux = groups.get(gd);
        aux.addTransaction(ts);
    }

    public synchronized void addTransaction(int gd, Transaction ts) {
        GroupDetails aux = groups.get(gd);
        aux.addTransaction(ts);
    }

    public GroupDetails getGroupPosition(int i) {
        return groups.get(i);
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("State{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", friends=").append(friends);
        sb.append(", groups=").append(groups);
        sb.append('}');
        return sb.toString();
    }

    public String getUserTs() {
        return userTs;
    }

    public void setUserTs(String userTs) {
        this.userTs = userTs;
    }
}
