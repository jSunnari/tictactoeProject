package server.datamodel;

/**
 * Entity-class for User
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
 */

import javax.persistence.*;

/**
 * Created by Jonas on 2016-05-19.
 */

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getUser",
                query = "select c FROM User c WHERE c.username = :userName"),
        @NamedQuery(
                name = "getAllStudents",
                query = "SELECT c FROM User c"),
        @NamedQuery(
                name = "getHighscore",
                query = "SELECT c FROM User c ORDER BY c.rank"),
})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private boolean login;
    private int player;
    private int wonMatches;
    private int tieMatches;
    private int lostMatches;
    private int rank;

    public User() {

    }

    //Create or update user:
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
