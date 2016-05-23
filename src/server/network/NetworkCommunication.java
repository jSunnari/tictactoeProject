package server.network;

/**
 * Network communication.
 * Handles the communication between server and client.
 */

import java.io.*;
import java.net.Socket;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkCommunication implements Runnable{

    private Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    public NetworkCommunication(Socket socket) {
        this.socket = socket;
        try {
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


    }
}
