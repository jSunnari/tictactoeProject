package client.network;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Jonas on 2016-05-23.
 */

public class NetworkConnection {

    private Socket socket;
    private boolean connected;
    private NetworkCommunication networkCommunication;

    public NetworkConnection(String ip, int port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 3000);
            networkCommunication = new NetworkCommunication(socket);
            Thread connectionThread = new Thread(networkCommunication);
            connectionThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        networkCommunication.disconnect();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkCommunication getNetworkCommunication(){
        return networkCommunication;
    }

}
