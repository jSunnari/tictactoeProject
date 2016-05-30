package client.network;



import client.logic.ClientController;

import java.io.IOException;
import java.net.*;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkConnection {

    private Socket socket;
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
            clientController.setConnectedToServer(true);

        } catch (SocketTimeoutException | UnknownHostException | ConnectException | IllegalArgumentException e){
            //If there is an timeout on the connection or an unknown host/port, call connectionFailed-method:
            clientController.connectionFailed();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(boolean exit){
        networkCommunication.disconnect(exit);
    }

    public NetworkCommunication getNetworkCommunication(){
        return networkCommunication;
    }

}
