package server.logic;

/**
 * ServerController,
 * Controller for the server-app.
 */

import server.datamodel.User;

import java.util.List;

/**
 * Created by Jonas on 2016-05-19.
 */

public class ServerController {
    private DatabaseConnection dbc;

    public ServerController() {
        dbc = new DatabaseConnection();
    }

    /**
     * Get user from database.
     * @param username
     * @return = user-object.
     */
    public User getUser(String username){
        return dbc.getUser(username);
    }

    /**
     * Create a user.
     * @param user = userobject.
     */
    public void createUser(User user){
        dbc.createUser(user);
    }

    /**
     * Update a user.
     * @param user = userobject.
     */
    public void updateUser(User user){
        int wonMatches = user.getWonMatches();
        int tieMatches = user.getTieMatches();

        int score = (wonMatches*3) + tieMatches;
        user.setRank(score);
        dbc.updateUser(user);
    }

    /**
     * Get highscore-list
     * @return = highscores as list.
     */
    public List<User> getHighscore(){
        return dbc.getHighscore();
    }

}
