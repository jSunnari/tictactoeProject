package server.network;

/**
 * Network communication.
 * Handles the communication between server and client.
 */

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
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private Map incommingMessage;


    public NetworkCommunication(Socket socket) {
        this.socket = socket;
        clientConnected = true;

    }

    @Override
    public void run() {

        try {
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            System.out.println(objectInputStream + " = objectinputstream"); //TEST
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (clientConnected) {

            try {
                if (objectInputStream.read() == 0){
                    System.out.println("disconnecting...");
                    disconnect();
                }
                else {
                    incommingMessage = (HashMap) objectInputStream.readObject();
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


            if (!incommingMessage.isEmpty() && incommingMessage != null) {

                if (incommingMessage.containsKey("createUser")) {
                    System.out.println(incommingMessage);
                }

            }

        }

    }

    void disconnect(){
        clientConnected = false;
        try {
            inputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
