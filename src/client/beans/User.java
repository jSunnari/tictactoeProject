package client.beans;

/**
 * Created by Jonas on 2016-05-19.
 */

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private int wonMatches;
    private int tieMatches;
    private int lostMatches;
    private int rank;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWonMatches() {
        return wonMatches;
    }

    public void setWonMatches(int wonMatches) {
        this.wonMatches = wonMatches;
    }

    public int getTieMatches() {
        return tieMatches;
    }

    public void setTieMatches(int tieMatches) {
        this.tieMatches = tieMatches;
    }

    public int getLostMatches() {
        return lostMatches;
    }

    public void setLostMatches(int lostMatches) {
        this.lostMatches = lostMatches;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
