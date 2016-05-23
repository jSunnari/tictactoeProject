package server.network;

/**
 * Network-listener.
 * Starts the server and waits for incomming clients.
 * Accepts the connections and creates an instance of the client class for every client and threads the clients.
 */

import server.logic.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jonas on 2016-05-19.
 */

public class NetworkListener implements Runnable {

    private ServerController serverController;
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private boolean connected = false;

    public NetworkListener(int port, ServerController controller) {
        try {
            this.port = port;
            this.serverController = controller;
            serverSocket = new ServerSocket(port);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Starting server on port " + port + " and waiting for clients...");

        while(connected){
            try {
                Thread.sleep(100);
                socket = new Socket();
                socket = serverSocket.accept();

                System.out.println("A client has connected!");

                Thread clientThread = new Thread(new NetworkCommunication(socket, serverController));
                clientThread.start();
            }

            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectServer(){
        connected = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
