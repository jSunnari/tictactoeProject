package server.network;

/**
 * Network communication for server.
 * Handles the communication between server and client.
 */

import server.datamodel.User;
import server.logic.ServerController;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkCommunication implements Runnable{

    private Socket socket;
    private boolean clientConnected;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private Map incommingMessage;
    private ServerController serverController;

    /**
     * Constructor sets boolean clientConnected to true.
     * @param socket = reference to socket, used for creating streams.
     */
    public NetworkCommunication(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
        clientConnected = true;
    }

    /**
     * Run-method,
     * Opens the inputstream and waits for incomming messages
     */
    @Override
    public void run() {

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            while (clientConnected) {

                if (objectInputStream.read() == 0){
                    System.out.println("Client has disconnected");
                    disconnect();
                    break;
                }
                else {
                    incommingMessage = (HashMap) objectInputStream.readObject();
                }


                if (incommingMessage != null && !incommingMessage.isEmpty()) {

                    if (incommingMessage.containsKey("createUser")) {
                        System.out.println(incommingMessage.get("createUser"));
                        User createdUser = (User) incommingMessage.get("createUser");
                        serverController.createUser(createdUser);

                    }

                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Disconnect-method,
     * Sets boolean clientConnected to false and closes both streams and socket.
     */
    void disconnect(){
        clientConnected = false;
        try {
            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
