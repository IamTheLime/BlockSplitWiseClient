package pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tiago-DESKTOP-WIN-PC on 17/05/13.
 */
public class GroupDetails implements Serializable {
    private String groupName;
    private String groupInfo;
    private ArrayList<String> members;
    private int photoId;

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

    public GroupDetails (String name, String groupInfo, int photoId) {
        this.groupName = name;
        this.groupInfo = groupInfo;
        this.photoId = photoId;
        this.members = new ArrayList<>();
    }

    public GroupDetails(GroupDetails gd) {
        this.groupName = gd.getGroupName();
        this.groupInfo = gd.getGroupInfo();
        this.photoId = gd.getPhotoId();
        this.members = gd.getMembers();
    }

    public GroupDetails(String groupName, String groupInfo, ArrayList<String> members, int photoId) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.photoId = photoId;
        this.members = new ArrayList<>();
        for (String s: members)
            this.members.add(s);
    }

    public GroupDetails clone() {
        return new GroupDetails(this);
    }
}