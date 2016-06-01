package client.beans;

/**
 * User-bean
 *
 * id = database-id.
 * username = users username.
 * password = users password.
 * email = users email.
 * login = if the user login was successful or not.
 * player = if player 1 or player 2.
 * won matches = how many won matches.
 * tie matches = how many tie matches.
 * lost matches = how many lost maches.
 * rank = won matches gets 3 points, tie matches gets 1 points, lost matches gets 0 points.
 * highscore = place in highscore-list.
 */

import java.io.Serializable;

/**
 * Created by Jonas on 2016-05-19.
 */

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String email;
    private boolean login;
    private int player;
    private int wonMatches;
    private int tieMatches;
    private int lostMatches;
    private int rank;
    private int highscore;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getHighScore() {
        return highscore;
    }

    public void setHighScore(int highScore) {
        this.highscore = highScore;
    }

    public boolean isLogin() {
        return login;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void setLogin(boolean login) {
        this.login = login;
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
