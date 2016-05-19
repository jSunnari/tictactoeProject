package server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jonas on 2016-05-19.
 */

public class NetworkListener implements Runnable {

    private ServerSocket serverSocket;
    private Socket connection;
    private boolean serverSocketOk = false;


    public NetworkListener(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocketOk = true;
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        System.out.println("Starting server...");

    }

    public void stopServerConnection(){
        serverSocketOk = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
    }

}
