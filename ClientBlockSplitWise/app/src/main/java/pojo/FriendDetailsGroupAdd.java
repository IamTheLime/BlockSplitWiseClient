package pojo;

import java.io.Serializable;

/**
 * Created by tiago on 6/24/17.
 */

public class FriendDetailsGroupAdd implements Serializable {
    private String friendName;

    public FriendDetailsGroupAdd(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
