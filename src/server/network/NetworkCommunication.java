package server.network;

/**
 * Network communication for server.
 * Threaded class.
 * Handles the communication between server and a client.
 */

import com.google.gson.Gson;
import server.beans.ConnectPlayers;
import server.beans.MarkerData;
import server.beans.Message;
import server.datamodel.User;
import server.logic.ServerController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkCommunication implements Runnable{

    private Socket socket;
    private boolean clientConnected;
    private Scanner input;
    private User currUser;
    private PrintWriter output;
    private ServerController serverController;
    private Gson gson;
    private ConnectPlayers connectPlayers;
    private GameController gameCommunication;

    /**
     * Constructor sets boolean clientConnected to true.
     * @param socket = reference to socket, used for creating streams.
     */
    public NetworkCommunication(Socket socket, ServerController serverController, GameController gameCommunication) {
        this.socket = socket;
        this.serverController = serverController;
        this.gameCommunication = gameCommunication;
        gson = new Gson();
        clientConnected = true;

        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run-method,
     * Opens the inputstream and waits for incomming messages
     */
    @Override
    public void run() {

        try {
            //Set up scanner:
            input = new Scanner(socket.getInputStream());
            String incommingMessage;

            //As long as the client is connected:
            while (clientConnected) {
                //And there is something in the stream:
                while (input.hasNext()) {
                    incommingMessage = input.nextLine();

                    //If it is a disconnect-message, disconnect:
                    if (incommingMessage.equals("disconnect")){
                        System.out.println("disconnecting client " + socket.getInetAddress());
                        disconnect();
                        break;
                    }
                    //else, send it to the parse-method:
                    else{
                        parse(incommingMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse-method,
     * Parses the data from json to POJO
     * @param jsonData = incomming data.
     */
    void parse (String jsonData){

        //Parse the data to a Message-object:
        Message currMessage = gson.fromJson(jsonData, Message.class);
        List<String> cmdData = currMessage.getCommandData();

        /**
         * Depending on what command comes through the stream:
         *
         * login = login-method for clients.
         * createUser = persists a user to the database.
         * updateUser = updates a user in the database.
         * getUpdatedUser = sends a user with the current data (highscore etc.).
         * getHighscore = sends the highscore-list.
         * ********** GAME COMMANDS **********
         * startGame = when a player wants to play, set player in a que until they are two players.
         * removeFromGameList = removes a player from the que.
         * stopGame = sends a command to the opponent that another player has quited the game.
         * markerData = gets info about where a marker has been set.
         * resetGame = sends command to both players to reset the game.
         * winningPlayer = sends command and winninplayer to both players.
         * playAgain = sends request to opponent to play again.
         */
        switch (currMessage.getCommand()) {

            //Login:
            case "login":
                //Get the userobject from the client containing only a username and password,
                User loginAttempt = gson.fromJson(cmdData.get(0), User.class);

                //Get the userobject from the database with the same username,
                User userReference = serverController.getUser(loginAttempt.getUsername());

                //If the user exists, and if the password matches,
                //Set the login-boolean to true and send the user-object from the database:
                if (userReference != null && loginAttempt.getPassword().equals(userReference.getPassword())){
                    userReference.setLogin(true);
                    send("login", userReference);
                    currUser = userReference;
                }
                //Else, send the same userobject which already has the login-boolean to false:
                else {
                    send("login", loginAttempt);
                }
                break;

            //Create a user:
            case "createUser":
                //Get the user to create:
                User createUserAttempt = gson.fromJson(cmdData.get(0), User.class);

                //If the user don't exist, create:
                if (serverController.getUser(createUserAttempt.getUsername()) == null) {
                    serverController.createUser(gson.fromJson(cmdData.get(0), User.class));
                    send("userCreated", "");
                }
                //Else, send command that user already exists:
                else {
                    send("userExists", " ");
                }
                break;

            //Update a user:
            case "updateUser":
                //User to update:
                User updateUser = gson.fromJson(cmdData.get(0), User.class);
                serverController.updateUser(updateUser);
                break;

            //Send an updated user (new data):
            case "getUpdatedUser":
                //User to get updated data from:
                User userToUpdate = gson.fromJson(cmdData.get(0), User.class);
                //Send back user:
                send("updateUser", serverController.getUser(userToUpdate.getUsername()));

            //Send highscore-list:
            case "getHighscore":
                send("getHighscore", serverController.getHighscore());
                break;

            /**
             * GAME-COMMANDS - the gamecontroller will take care of these: **
             */
            //Start a game, add to playerlist:
            case "startGame":
                //Create a "ConnectPlayers"-object with current user:
                connectPlayers = new ConnectPlayers(currUser, this);
                //Send to the gamecontroller:
                gameCommunication.connectPlayers(connectPlayers);
                break;

            //Remove a player from the playerlist:
            case "removeFromGameList":
                //Remove the user from gamecontroller:
                gameCommunication.removePlayer(currUser);
                break;

            //Player who stoppes a game, sends a command to the opponent and removes the user from the playerlist:
            case "stopGame":
                User opponentUser = gson.fromJson(cmdData.get(0), User.class);
                gameCommunication.updateClient("opponentStoppedGame", opponentUser.getUsername());
                //Remove the user from gamecontroller:
                gameCommunication.removePlayer(currUser);
                break;

            //Get marker data and send to client:
            case "markerData":
                MarkerData markerData = gson.fromJson(cmdData.get(0), MarkerData.class);
                //Find opponent and send data:
                gameCommunication.updateClient("drawMarker", markerData.getOpponentUsername(), markerData);
                //Send back data to player:
                send("drawMarker", markerData);
                break;

            //Reset again - (play again):
            case "resetGame":
                //Opponent:
                User opponent = gson.fromJson(cmdData.get(0), User.class);
                //Send command to reset the game for opponent:
                gameCommunication.updateClient("resetGame", opponent.getUsername());
                //Send command to reset the game for the current player:
                send("resetGame", "");
                break;

            //The player who wins the round:
            case "winningPlayer":
                //Opponent:
                User opponentPlayer = gson.fromJson(cmdData.get(0), User.class);
                //Send command and the player who won:
                send("winningPlayer", currUser);
                //Send command and the player who won:
                gameCommunication.updateClient("winningPlayer", opponentPlayer.getUsername(), currUser);
                break;

            //Sends request to play again:
            case "playAgain":
                //Opponent:
                User opponentUsr = gson.fromJson(cmdData.get(0), User.class);
                //Send request to play again to opponent:
                gameCommunication.updateClient("playAgain", opponentUsr.getUsername());
                break;
        }
    }

    /**
     * Send method,
     * Sends data to client.
     * @param command = what kind of command (check api-commands.txt)
     * @param cmdData = the data (generic).
     */
    <T> void send (String command, T cmdData){
        Message currMessage = new Message(command, cmdData);
        String jsonData = gson.toJson(currMessage);
        output.println(jsonData);
    }

    public Socket getSocket(){
        return socket;
    }

    /**
     * Disconnect-method,
     * Sets boolean clientConnected to false and closes both streams and socket.
     */
    void disconnect(){
        clientConnected = false;
        gameCommunication.removePlayer(currUser);
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
