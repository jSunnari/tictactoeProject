package client.network;

/**
 * Network communication for client,
 * Handles the communication between server and client.
 */

import client.beans.MarkerData;
import client.beans.Message;
import client.beans.User;
import client.logic.ClientController;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkCommunication implements Runnable {
    private PrintWriter output;
    private Scanner input;
    private Gson gson;
    private Socket socket;
    private ClientController clientController;

    /**
     * @param socket = reference to the socket, used for creating streams.
     */
    public NetworkCommunication(Socket socket, ClientController clientController) {
        this.socket = socket;
        this.clientController = clientController;
        gson = new Gson();
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listens for incomming messages:
     */
    @Override
    public void run() {

        try {
            //Set up scanner:
            input = new Scanner(socket.getInputStream());
            String incommingMessage;

            //While there is something in the stream, send to parse-method:
            while (input.hasNext()) {
                incommingMessage = input.nextLine();
                parse(incommingMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse-method,
     * Parses from json to POJO.
     * @param jsonData
     */
    void parse (String jsonData){

        //Parse from JSON to a Message-object:
        Message currMessage = gson.fromJson(jsonData, Message.class);
        List<String> cmdData = currMessage.getCommandData();

        /**
         * Depending on what command comes through the stream:
         */
        switch (currMessage.getCommand()) {

            //login:
            case "login":
                User loginUser = gson.fromJson(cmdData.get(0), User.class);
                clientController.setCurrentUser(loginUser);
                break;

            //If a user already exists:
            case "userExists":
                clientController.createAccountResponse("error", "Username already exists.");
                break;

            //Confirmation that a user has been created:
            case "userCreated":
                clientController.createAccountResponse("created", "User has been created!");
                break;

            //Get current user with updated data from database:
            case "updateUser":
                User updateUser = gson.fromJson(cmdData.get(0), User.class);
                clientController.updateCurrUser(updateUser);
                break;

            //Get the highscore-list:
            case "getHighscore":
                User[] highscoreList = gson.fromJson(cmdData.get(0), User[].class);

                clientController.clearHighscoreList();

                for (User highScoreUser : highscoreList){
                    clientController.addToHighscoreList(highScoreUser);
                }
                break;

            /**
             * GAME COMMANDS:
             */
            //Start a game:
            case "startGame":
                User opponentUser = gson.fromJson(cmdData.get(0), User.class);
                clientController.opponentConnected(opponentUser);
                break;

            //Draw a marker - (X or O):
            case "drawMarker":
                MarkerData markerData = gson.fromJson(cmdData.get(0), MarkerData.class);
                clientController.drawMarker(markerData);
                break;

            //Set player1:
            case "setPlayer1":
                User player1 = gson.fromJson(cmdData.get(0), User.class);
                clientController.setCurrentUser(player1);
                break;

            //Set player2:
            case "setPlayer2":
                User player2 = gson.fromJson(cmdData.get(0), User.class);
                clientController.setCurrentUser(player2);
                break;

            //Resets the game, someone pressed play again:
            case "resetGame":
                clientController.resetGame();
                break;

            //The winning player of a round:
            case "winningPlayer":
                User winningPlayer = gson.fromJson(cmdData.get(0), User.class);
                clientController.setScore(winningPlayer);
                break;

            //If an opponent stopped a game (exit):
            case "opponentStoppedGame":
                clientController.opponentStoppedGame();
                break;
        }
    }

    /**
     * Send method,
     * Sends data to client.
     * @param cmd = what kind of command (check api-commands.txt)
     * @param cmdData = the data (generic)
     */
    public <T> void send(String cmd, T cmdData) {
        Message currMessage = new Message(cmd, cmdData);

        output.println(gson.toJson(currMessage));
    }

    /**
     * Disconnect-method,
     * Sends a message to the server as a command for disconnect.
     * Closes the stream there after and if exit-boolean is true, the application closes too.
     */
    public void disconnect(boolean exit) {
        output.println("disconnect");
        output.flush();
        output.close();

        try {
            socket.close();
            clientController.setConnectedToServer(false);
            if (exit) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
