package server.network;

/**
 * Network-listener.
 * Starts the server and waits for incomming clients.
 * Accepts the connections and creates an instance of the client class for every client and threads the clients.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jonas on 2016-05-19.
 */

public class NetworkListener implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private boolean connected = false;

    public NetworkListener(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(port);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Starting server and waiting for clients...");

        while(connected){
            try {
                Thread.sleep(100);
                socket = new Socket();
                socket = serverSocket.accept();

                System.out.println("Ip: " + socket.getInetAddress());
                System.out.println("Port: " + port);

                Thread clientThread = new Thread(new NetworkCommunication(socket));
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
