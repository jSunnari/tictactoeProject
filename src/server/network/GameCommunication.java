package server.network;

import com.google.gson.Gson;
import server.beans.ConnectPlayers;
import server.datamodel.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 2016-05-25.
 */

public class GameCommunication {


    //List holding users that is currently playing:
    private List<ConnectPlayers> connectPlayers;
    private int counter = 0;
    private Gson gson;



    public GameCommunication() {
        gson = new Gson();
        connectPlayers = new ArrayList<ConnectPlayers>();

    }


    public void connectPlayers(ConnectPlayers user){
        counter++;
        connectPlayers.add(user);
        if (counter == 2){
            User player1 = connectPlayers.get(0).getUser();
            User player2 = connectPlayers.get(1).getUser();

            connectPlayers.get(0).getNetworkCommunication().send("startGame", player2);
            connectPlayers.get(1).getNetworkCommunication().send("startGame", player1);

            counter = 0;
        }
    }

    /**
     * Send to opponent player.
     *
     * @param user = user to send to.
     * @param command = command to send.
     */
    public void updateClient(User user, String command){
        for (ConnectPlayers player : connectPlayers){
            if (user.getUsername().equals(player.getUser().getUsername())){
                player.getNetworkCommunication().send(command, "");
                break;
            }
        }
    }



    /**
     * Remove a player from currently playing-list:
     * @param user = user to remove (if exists)
     */
    public void removePlayer(User user){
        for (ConnectPlayers player : connectPlayers){
            if (user.getUsername().equals(player.getUser().getUsername())){
                connectPlayers.remove(player);
                break;
            }
        }
    }
}
