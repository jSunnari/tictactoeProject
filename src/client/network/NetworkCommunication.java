package client.network;

/**
 * Network communication for client,
 * Handles the communication between server and client.
 */

import client.beans.User;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkCommunication implements Runnable {
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Map incommingMessage;
    private Socket socket;

    /**
     * @param socket = reference to the socket, used for creating streams.
     */
    public NetworkCommunication(Socket socket) {
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("run-metod");//test

    }


    public User login(User user){
        //SKICKA LOGIN OCH SKICKA TILLBAKA USER

        return user;
    }

    public void createUser(User user){


        Map<String, User> createUser = new HashMap<String, User>();
        createUser.put("createUser", user);

        try {
            objectOutputStream.writeObject(createUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User updateUser(User user){
        //UPPDATERA OCH SKICKA TILLBAKA USER

        return user;
    }

    public void getHighScore(){
        //SKICKA TILLBAKA LISTA MED HIGHSCORES
    }


    /**
     * Disconnect-method,
     * Sends a 0 to the server as a command for exit.
     * Closes the stream there after.
     */
    public void disconnect() {
        try {
            objectOutputStream.write(0);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
