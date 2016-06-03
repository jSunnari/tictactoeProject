package server;

import server.logic.ServerController;
import server.network.NetworkListener;

/**
 * Created by Jonas on 2016-05-19.
 */

public class ServerApp {

    public static void main(String[] args) {

        int port = 50123;

        // Starts controller
        ServerController controller = new ServerController();

        /* Port is always 50123
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the port: ");
        port = scanner.nextInt();
        */

        //Method that closes database just before the system quits:
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                controller.disconnect();
            }
        }));

        // Starts network listening in separate thread
        NetworkListener networkListener = new NetworkListener(port, controller);
        Thread networkThread = new Thread(networkListener);
        networkThread.start();
    }

}
