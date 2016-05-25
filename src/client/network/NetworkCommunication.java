package client.network;

/**
 * Network communication for client,
 * Handles the communication between server and client.
 */

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
            case "login":
                User loginUser = gson.fromJson(cmdData.get(0), User.class);

                clientController.setCurrentUser(loginUser);

                break;

            case "userExists":
                clientController.createAccountResponse(true);
                break;

            case "userCreated":
                clientController.createAccountResponse(false);
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
     * Sends a message to the server as a command for exit.
     * Closes the stream there after.
     */
    public void disconnect() {
        output.println("disconnect");
        output.close();
    }

}
