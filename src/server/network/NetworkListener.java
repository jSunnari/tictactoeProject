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
    private GameController gameCommunication;

    public NetworkListener(int port, ServerController controller) {
        try {
            this.port = port;
            this.serverController = controller;
            serverSocket = new ServerSocket(port);
            gameCommunication = new GameController();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Starting server on port " + port + " and waiting for clients...");

        /**
         * Listens for new clients:
         */
        while(connected){
            try {
                Thread.sleep(200);
                socket = new Socket();
                socket = serverSocket.accept();

                //When a client has connected:
                System.out.println("A client has connected from " + socket.getInetAddress());

                //Creates an object of the NetworkCommunication and starts a new thread:
                NetworkCommunication networkCommunication = new NetworkCommunication(socket, serverController, gameCommunication);
                Thread clientThread = new Thread(networkCommunication);

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
            e.printStackTrace();
        }
    }

}
