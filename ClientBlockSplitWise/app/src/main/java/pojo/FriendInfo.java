package pojo;

import com.blocksplitwise.clientblocksplitwise.Friend;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by rui on 23-06-2017.
 */

public class FriendInfo implements Serializable {
    private String friendName;
    private int photoId;

    public FriendInfo(String friendName, int photoId) {
        this.friendName = friendName;
        this.photoId = photoId;
    }

    public String getfriendName() {
        return friendName;
    }

    public int getPhotoId() {
        return photoId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FriendInfo{");
        sb.append("friendName='").append(friendName).append('\'');
        sb.append(", photoId=").append(photoId);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendInfo that = (FriendInfo) o;
        return photoId == that.photoId &&
                Objects.equals(friendName, that.friendName);
    }

    public FriendInfo clone() {
        return new FriendInfo(this.friendName,this.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendName, photoId);
    }
}
