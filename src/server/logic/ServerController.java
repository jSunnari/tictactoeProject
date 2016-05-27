package server.logic;

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

    public User getUser(String username){
        return dbc.getUser(username);
    }

    public void createUser(User user){
        dbc.createUser(user);
    }

    public void updateUser(User user){
        dbc.updateUser(user);
    }

    public List<User> getHighscore(){
        return dbc.getHighscore();
    }

}
