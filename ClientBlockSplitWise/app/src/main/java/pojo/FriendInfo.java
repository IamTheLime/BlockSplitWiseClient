package pojo;

/**
 * Created by rui on 23-06-2017.
 */

public class FriendInfo {
    private String friendName;
    private int photoId;

    public FriendInfo(String friendName, int photoId) {
        this.friendName = friendName;
        this.photoId = photoId;
    }

    public String getfriendName() {
        return friendName;
    }

    public void setfriendName(String friendName) {
        this.friendName = friendName;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FriendInfo{");
        sb.append("friendName='").append(friendName).append('\'');
        sb.append(", photoId=").append(photoId);
        sb.append('}');
        return sb.toString();
    }
}
