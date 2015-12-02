package innoworld.cyclingmaster.model;

import java.io.Serializable;

/**
 * Created by latifalbar on 12/3/2015.
 */
public class User implements Serializable {

    public String email;

    public String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
