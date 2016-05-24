package server.network;

/**
 * Network communication for server.
 * Handles the communication between server and client.
 */

import com.google.gson.Gson;
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
    private PrintWriter output;
    private ServerController serverController;
    private Gson gson;

    /**
     * Constructor sets boolean clientConnected to true.
     * @param socket = reference to socket, used for creating streams.
     */
    public NetworkCommunication(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
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
                        System.out.println("disconnecting client..");
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
         */
        switch (currMessage.getCommand()) {

            case "login":
                User loginAttempt = gson.fromJson(cmdData.get(0), User.class);
                User userReference = serverController.getUser(loginAttempt.getUsername());

                if (userReference == null){
                    loginAttempt.setLogin(false);
                }
                else if (loginAttempt.getPassword().equals(userReference.getPassword())){
                    loginAttempt.setLogin(true);
                }

                send("login", loginAttempt);

                break;

            case "createUser":
                User createUserAttempt = gson.fromJson(cmdData.get(0), User.class);

                if (serverController.getUser(createUserAttempt.getUsername()) == null) {
                    serverController.createUser(gson.fromJson(cmdData.get(0), User.class));
                    send("userCreated", "");
                }
                else {
                    send("userExists", " ");
                }
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

    /**
     * Disconnect-method,
     * Sets boolean clientConnected to false and closes both streams and socket.
     */
    void disconnect(){
        clientConnected = false;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
