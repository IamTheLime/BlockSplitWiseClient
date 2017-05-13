package pojo;

import java.io.Serializable;

/**
 * Created by Tiago-DESKTOP-WIN-PC on 17/05/13.
 */
public class GroupDetails implements Serializable {
    private String groupName;
    private String groupInfo;
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

    public GroupDetails (String name, String groupInfo, int photoId) {
        this.groupName = name;
        this.groupInfo = groupInfo;
        this.photoId = photoId;
    }
}