package innoworld.cyclingmaster.model;

import java.io.Serializable;

/**
 * Created by latifalbar on 12/3/2015.
 */
public class LoginReponse implements Serializable {

    public String tag;

    public String error;

    public String uid;

    public User user;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
