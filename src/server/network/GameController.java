package server.network;

/**
 * Game-communication,
 *
 * When a player wants to play, the player will send a reference to its own userobject and also a reference to its own
 * NetworkCommunication-class inside a "ConnectPlayers"-object.
 *
 * The ConnectPlayers-reference will be stored in an arraylist, and will be removed when the player has finished playing.
 *
 * There is a counter that checks when two players has connected.
 */

import server.beans.ConnectPlayers;
import server.datamodel.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 2016-05-25.
 */

public class GameController {

    //List holding users that is currently playing:
    private List<ConnectPlayers> connectPlayers;
    //Counter checking when its time to start a game:
    private int counter = 0;

    public GameController() {
        connectPlayers = new ArrayList<ConnectPlayers>();
    }

    /**
     * Connect players for a match.
     *
     * Adds the Connect
     *
     * @param user = object including a reference to the user and a reference to its own threaded class for communication.
     */
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
     * Send a message to the opponent player.
     *
     * When a player wants to send a message to an opponent player, the player sends a reference to the opponentplayer,
     * this method will den search the arraylist for that certain username.
     *
     * When it finds it, it will send the message via the already existing NetworkCommunication-class(threaded class for the
     * opponent user).
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
