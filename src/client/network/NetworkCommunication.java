package client.network;

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
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private Map incommingMessage;
    private Socket socket;


    private boolean connected;

    public NetworkCommunication(Socket socket) {
        this.socket = socket;
        try {
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("objectoutputstream = " + objectOutputStream);
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
            //objectOutputStream.close();
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


    public void disconnect() {
        try {
            objectOutputStream.write(0);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
