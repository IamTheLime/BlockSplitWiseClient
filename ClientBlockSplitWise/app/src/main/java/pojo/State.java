package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 28/06/2017.
 */

public class State implements Serializable{

    private String userName;
    private List<FriendInfo> friends;
    private List<GroupDetails> groups;

    public State(String userName, List<FriendInfo> friends, List<GroupDetails> groups) {
        this.userName = userName;
        this.friends = friends;
        this.groups = groups;
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

    public void addGroup(GroupDetails gd) {
        groups.add(gd);
    }

    public void addFriend(FriendInfo fi) {
        friends.add(fi);
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
}
