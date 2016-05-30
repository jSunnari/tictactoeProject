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
import server.beans.MarkerData;
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
     * The ConnectPlayers-object including a reference to the Players User-object and the players NetworkCommunication-class
     * will be added to an arraylist and the counter will be increased with 1.
     * When two players has connected, the server will set the first user to player 1 and the seconds player to player 2,
     * the server will then send a command to each player with an object of the opponentuser and set the counter to 0 again.
     *
     * @param user = The player who is in the que for playing.
     */
    public void connectPlayers(ConnectPlayers user){
        counter++;
        connectPlayers.add(user);
        if (counter == 2){
            User player1 = connectPlayers.get(connectPlayers.size()-2).getUser();
            player1.setPlayer(1);

            User player2 = connectPlayers.get(connectPlayers.size()-1).getUser();
            player2.setPlayer(2);

            connectPlayers.get(connectPlayers.size()-2).getNetworkCommunication().send("setPlayer1", player1);
            connectPlayers.get(connectPlayers.size()-1).getNetworkCommunication().send("setPlayer2", player2);

            connectPlayers.get(connectPlayers.size()-2).getNetworkCommunication().send("startGame", player2);
            connectPlayers.get(connectPlayers.size()-1).getNetworkCommunication().send("startGame", player1);

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
     */
    public void updateClient(String command, String user, MarkerData markerData){
        for (ConnectPlayers player : connectPlayers){
            if (user.equals(player.getUser().getUsername())) {
                player.getNetworkCommunication().send(command, markerData);
            }
        }
    }

    /**
     * Update a client with information about the game.
     *
     * When a player wants to send data to the players opponent, the command and data will come here,
     * This method will then search the arraylist for the oppoentusers NetworkCommunication-class and send data trough it.
     *
     * @param command = the command to send.
     * @param opponentPlayer = the oppoentPlayer.
     * @param winningPlayer = if the game ended, this is the winningplayer.
     */
    public void updateClient(String command, String opponentPlayer, User winningPlayer){
        for (ConnectPlayers player : connectPlayers){
            if (opponentPlayer.equals(player.getUser().getUsername())) {
                player.getNetworkCommunication().send(command, winningPlayer);
            }
        }
    }
    //Same as above but overloaded:
    public void updateClient(String command, String user){
        for (ConnectPlayers player : connectPlayers){
            if (user.equals(player.getUser().getUsername())) {
                player.getNetworkCommunication().send(command, "");
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
                counter = 0;
                break;
            }
        }
    }
}
