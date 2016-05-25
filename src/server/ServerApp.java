package server;

import server.logic.ServerController;
import server.network.NetworkListener;

import java.util.Scanner;

/**
 * Created by Jonas on 2016-05-19.
 */

public class ServerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int port;

        // Starts controller
        ServerController controller = new ServerController();

        System.out.println("Please input the port: ");
        port = scanner.nextInt();
        // Starts network listening in separate thread
        NetworkListener networkListener = new NetworkListener(port, controller);

        Thread networkThread = new Thread(networkListener);
        networkThread.start();
    }

}
