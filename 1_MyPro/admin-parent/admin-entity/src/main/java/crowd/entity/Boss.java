package crowd.entity;

import java.io.Serializable;

/**
 * @author Zhaoy
 * @creat 2022-03-28-15:31
 */
public class Boss implements Serializable {

    private String username;
    private String hobby;

    public Boss(String username, String hobby) {
        this.username = username;
        this.hobby = hobby;
    }

    public Boss() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "username='" + username + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
