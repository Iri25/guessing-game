package domain;

import java.util.Objects;

public class Player extends Entity<Integer> {

    private String username;
    private String password;

    public Player(){}

    public Player(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
