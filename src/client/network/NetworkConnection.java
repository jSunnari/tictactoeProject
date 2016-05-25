package client.network;



import client.logic.ClientController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkConnection {

    private Socket socket;
    private boolean connected;
    private NetworkCommunication networkCommunication;
    private ClientController clientController;

    public NetworkConnection(String ip, int port, ClientController clientController) {
        this.clientController = clientController;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 3000);
            networkCommunication = new NetworkCommunication(socket, clientController);
            Thread connectionThread = new Thread(networkCommunication);
            connectionThread.start();


        } catch (SocketTimeoutException | UnknownHostException e){
            //If there is an timeout on the connection or an unknown host, call connectionFailed-method:
            clientController.connectionFailed();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        networkCommunication.disconnect();

        try {
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkCommunication getNetworkCommunication(){
        return networkCommunication;
    }

}
